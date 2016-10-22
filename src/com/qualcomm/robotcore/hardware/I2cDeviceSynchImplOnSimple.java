/*
 * Copyright (c) 2016 Robert Atkinson
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * 
 * Neither the name of Robert Atkinson nor the names of his contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 * 
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.qualcomm.robotcore.hardware;

import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * I2cDeviceSynchImplOnSimple takes an I2cDeviceSynchSimple and adds to it heartbeat and
 * readwindow functionality.
 */
public class I2cDeviceSynchImplOnSimple implements I2cDeviceSynch {
	// ----------------------------------------------------------------------------------------------
	// State
	// ----------------------------------------------------------------------------------------------

	protected I2cDeviceSynchSimple i2cDeviceSynchSimple;
	protected boolean isSimpleOwned;

	protected ReadWindow readWindow; // the set of registers to look at when we are in read mode. May be null, indicating no
// read needed

	protected int iregReadLast, cregReadLast;
	protected int iregWriteLast;
	protected byte[] rgbWriteLast;

	protected boolean isHooked; // whether we are connected to the underling device or not
	protected boolean isEngaged; // user's hooking *intent*
	protected boolean isClosing;

	protected int msHeartbeatInterval; // time between heartbeats; zero is 'none necessary'
	protected HeartbeatAction heartbeatAction; // the action to take when a heartbeat is needed. May be null.
	protected ScheduledExecutorService heartbeatExecutor; // used to schedule heartbeats when we need to read from the outside

	protected final Object engagementLock = new Object();
	protected final Object concurrentClientLock = new Object(); // the lock we use to serialize against concurrent clients of
// us.

	// ----------------------------------------------------------------------------------------------
	// Construction
	// ----------------------------------------------------------------------------------------------

	public I2cDeviceSynchImplOnSimple(final I2cDeviceSynchSimple simple, final boolean isSimpleOwned) {
		i2cDeviceSynchSimple = simple;
		this.isSimpleOwned = isSimpleOwned;

		msHeartbeatInterval = 0;
		heartbeatAction = null;
		heartbeatExecutor = null;
		readWindow = null;
		cregReadLast = 0;
		rgbWriteLast = null;
		isEngaged = false;
		isHooked = false;
		isClosing = false;
	}

	// ----------------------------------------------------------------------------------------------
	// Logging
	// ----------------------------------------------------------------------------------------------

	@Override
	public void setLogging(final boolean enabled) {
		i2cDeviceSynchSimple.setLogging(enabled);
	}

	@Override
	public void setLoggingTag(final String loggingTag) {
		i2cDeviceSynchSimple.setLoggingTag(loggingTag);
	}

	// ----------------------------------------------------------------------------------------------
	// Engagable
	// ----------------------------------------------------------------------------------------------

	@Override
	public void engage() {
		// The engagement lock is distinct from the concurrentClientLock because we need to be
		// able to drain heartbeats while disarming, so can't own the concurrentClientLock then,
		// but we still need to be able to lock out engage() and disengage() against each other.
		// Locking order: armingLock > concurrentClientLock > callbackLock
		//
		synchronized (engagementLock) {
			isEngaged = true;
			adjustHooking();
		}
	}

	protected void hook() {
		// engagementLock is distinct from the concurrentClientLock because we need to be
		// able to drain heartbeats while disarming, so can't own the concurrentClientLock then,
		// but we still need to be able to lock out engage() and disengage() against each other.
		// Locking order: engagementLock > concurrentClientLock
		//
		synchronized (engagementLock) {
			if (!isHooked) {
				startHeartBeat();
				isHooked = true;
			}
		}
	}

	/* adjust the hooking state to reflect the user's engagement intent */
	protected void adjustHooking() {
		synchronized (engagementLock) {
			if (!isHooked && isEngaged)
				hook();
			else if (isHooked && !isEngaged) unhook();
		}
	}

	@Override
	public boolean isEngaged() {
		return isEngaged;
	}

	@Override
	public boolean isArmed() {
		synchronized (engagementLock) {
			if (isHooked) {
				return i2cDeviceSynchSimple.isArmed();
			}
			return false;
		}
	}

	@Override
	public void disengage() {
		synchronized (engagementLock) {
			isEngaged = false;
			adjustHooking();
		}
	}

	protected void unhook() {
		synchronized (engagementLock) {
			if (isHooked) {
				stopHeartBeat();
				synchronized (concurrentClientLock) {
					waitForWriteCompletions();
					isHooked = false;
				}
			}
		}
	}

	// ----------------------------------------------------------------------------------------------
	// Heartbeats
	// ----------------------------------------------------------------------------------------------

	@Override
	public void setHeartbeatInterval(final int ms) {
		synchronized (concurrentClientLock) {
			msHeartbeatInterval = Math.max(0, msHeartbeatInterval);
			stopHeartBeat();
			startHeartBeat();
		}
	}

	@Override
	public int getHeartbeatInterval() {
		return msHeartbeatInterval;
	}

	@Override
	public void setHeartbeatAction(final HeartbeatAction action) {
		heartbeatAction = action;
	}

	@Override
	public HeartbeatAction getHeartbeatAction() {
		return heartbeatAction;
	}

	void startHeartBeat() {
		if (msHeartbeatInterval > 0) {
			heartbeatExecutor = ThreadPool.newSingleThreadScheduledExecutor();
			heartbeatExecutor.scheduleAtFixedRate(() -> {
				final HeartbeatAction action = getHeartbeatAction();
				if (action != null) {
					synchronized (concurrentClientLock) {
						if (action.rereadLastRead && cregReadLast != 0) {
							read(iregReadLast, cregReadLast);
							return;
						}
						if (action.rewriteLastWritten && rgbWriteLast != null) {
							write(iregWriteLast, rgbWriteLast);
							return;
						}
						if (action.heartbeatReadWindow != null) {
							read(action.heartbeatReadWindow.getRegisterFirst(), action.heartbeatReadWindow.getRegisterCount());
						}
					}
				}
			}, 0, msHeartbeatInterval, TimeUnit.MILLISECONDS);
		}
	}

	void stopHeartBeat() {
		if (heartbeatExecutor != null) {
			heartbeatExecutor.shutdownNow();
			ThreadPool.awaitTerminationOrExitApplication(heartbeatExecutor, 2, TimeUnit.SECONDS, "heartbeat executor", "internal error");
			heartbeatExecutor = null;
		}
	}

	// ----------------------------------------------------------------------------------------------
	// Read window
	// ----------------------------------------------------------------------------------------------

	@Override
	public void setReadWindow(final ReadWindow window) {
		synchronized (concurrentClientLock) {
			readWindow = window.readableCopy();
		}
	}

	@Override
	public ReadWindow getReadWindow() {
		synchronized (concurrentClientLock) {
			return readWindow;
		}
	}

	@Override
	public void ensureReadWindow(final ReadWindow windowNeeded, final ReadWindow windowToSet) {
		synchronized (concurrentClientLock) {
			if (readWindow == null || !readWindow.containsWithSameMode(windowNeeded)) {
				setReadWindow(windowToSet);
			}
		}
	}

	@Override
	public TimestampedData readTimeStamped(final int ireg, final int creg, final ReadWindow readWindowNeeded, final ReadWindow readWindowSet) {
		ensureReadWindow(readWindowNeeded, readWindowSet);
		return readTimeStamped(ireg, creg);
	}

	// ----------------------------------------------------------------------------------------------
	// HardwareDevice
	// ----------------------------------------------------------------------------------------------

	@Override
	public Manufacturer getManufacturer() {
		return i2cDeviceSynchSimple.getManufacturer();
	}

	@Override
	public String getDeviceName() {
		return i2cDeviceSynchSimple.getDeviceName();
	}

	@Override
	public String getConnectionInfo() {
		return i2cDeviceSynchSimple.getConnectionInfo();
	}

	@Override
	public int getVersion() {
		return i2cDeviceSynchSimple.getVersion();
	}

	@Override
	public void resetDeviceConfigurationForOpMode() {
		i2cDeviceSynchSimple.resetDeviceConfigurationForOpMode();
		readWindow = null;
		// TODO: more to come
	}

	@Override
	public void close() {
		isClosing = true;
		disengage();
		if (isSimpleOwned) {
			i2cDeviceSynchSimple.close();
		}
	}

	// ----------------------------------------------------------------------------------------------
	// I2cDeviceSynch pass through
	// ----------------------------------------------------------------------------------------------

	protected boolean isOpenForReading() {
		return isHooked && newReadsAndWritesAllowed();
	}

	protected boolean isOpenForWriting() {
		return isHooked && newReadsAndWritesAllowed();
	}

	protected boolean newReadsAndWritesAllowed() {
		return !isClosing;
	}

	@Override
	public void setI2cAddress(final I2cAddr newAddress) {
		setI2cAddr(newAddress);
	}

	@Override
	public I2cAddr getI2cAddress() {
		return getI2cAddr();
	}

	@Override
	public void setI2cAddr(final I2cAddr i2cAddr) {
		i2cDeviceSynchSimple.setI2cAddr(i2cAddr);
	}

	@Override
	public I2cAddr getI2cAddr() {
		return i2cDeviceSynchSimple.getI2cAddr();
	}

	@Override
	public synchronized byte read8(final int ireg) {
		return read(ireg, 1)[0];
	}

	@Override
	public byte[] read(final int ireg, final int creg) {
		return readTimeStamped(ireg, creg).data;
	}

	@Override
	public TimestampedData readTimeStamped(final int ireg, final int creg) {
		synchronized (concurrentClientLock) {
			if (!isOpenForReading()) return I2cDeviceSynchImpl.makeFakeData(ireg, creg);

			iregReadLast = ireg;
			cregReadLast = creg;
			if (readWindow != null && readWindow.contains(ireg, creg)) {
				final TimestampedData windowedData = i2cDeviceSynchSimple.readTimeStamped(readWindow.getRegisterFirst(), readWindow.getRegisterCount());
				final int ibFirst = ireg - readWindow.getRegisterFirst();
				final TimestampedData result = new TimestampedData();
				result.data = Arrays.copyOfRange(windowedData.data, ibFirst, ibFirst + creg);
				result.nanoTime = windowedData.nanoTime;
				return result;
			} else {
				return i2cDeviceSynchSimple.readTimeStamped(ireg, creg);
			}
		}
	}

	@Override
	public void write8(final int ireg, final int bVal) {
		write8(ireg, bVal, false);
	}

	@Override
	public void write8(final int ireg, final int bVal, final boolean waitForCompletion) {
		write(ireg, new byte[] {(byte) bVal}, waitForCompletion);
	}

	@Override
	public void write(final int ireg, final byte[] data) {
		write(ireg, data, false);
	}

	@Override
	public void write(final int ireg, final byte[] data, final boolean waitForCompletion) {
		synchronized (concurrentClientLock) {
			if (!isOpenForWriting()) return; // Ignore the write

			iregWriteLast = ireg;
			rgbWriteLast = Arrays.copyOf(data, data.length);
			i2cDeviceSynchSimple.write(ireg, data, waitForCompletion);
		}
	}

	@Override
	public void waitForWriteCompletions() {
		i2cDeviceSynchSimple.waitForWriteCompletions();
	}

	@Override
	public void enableWriteCoalescing(final boolean enable) {
		i2cDeviceSynchSimple.enableWriteCoalescing(enable);
	}

	@Override
	public boolean isWriteCoalescingEnabled() {
		return i2cDeviceSynchSimple.isWriteCoalescingEnabled();
	}
}
