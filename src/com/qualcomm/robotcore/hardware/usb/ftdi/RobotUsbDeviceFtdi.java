/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.robotcore.hardware.usb.ftdi;

import android.support.annotation.NonNull;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;
import com.qualcomm.robotcore.exception.FTDeviceClosedException;
import com.qualcomm.robotcore.exception.FTDeviceIncompleteInitializationException;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;

import junit.framework.Assert;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class RobotUsbDeviceFtdi implements RobotUsbDevice {

  // contains all the devices currently opened
  protected static final ConcurrentHashMap<SerialNumber, RobotUsbDeviceFtdi> extantDevices = new ConcurrentHashMap<SerialNumber, RobotUsbDeviceFtdi>();
  protected static final ConcurrentHashMap<SerialNumber, DeviceManager.DeviceType> deviceTypes = new ConcurrentHashMap<SerialNumber, DeviceManager.DeviceType>();

  private FT_Device       device;
  private FirmwareVersion firmwareVersion;
  private boolean         purgeNeeded;
  private byte            purgeWhat;
  private boolean         interruptRequested;
  private SerialNumber    serialNumber;
  private DeviceManager.DeviceType deviceType;
  private final static Object writeLock = new Object();

  public RobotUsbDeviceFtdi(FT_Device device, SerialNumber serialNumber) {
    this.device = device;
    this.firmwareVersion = null;
    this.purgeNeeded = false;
    this.purgeWhat = 0;
    this.interruptRequested = false;
    this.serialNumber = serialNumber;
    this.deviceType = deviceTypes.get(this.serialNumber);
    if (this.deviceType == null) this.deviceType = DeviceManager.DeviceType.UNKNOWN_DEVICE;

    Assert.assertFalse(extantDevices.contains(serialNumber));
    extantDevices.put(serialNumber, this);
  }

  public static Collection<RobotUsbDeviceFtdi> getExtantDevices() {
    return extantDevices.values();
  }

  @Override @NonNull public SerialNumber getSerialNumber() {
    return this.serialNumber;
  }

  @Override public void setDeviceType(@NonNull DeviceManager.DeviceType deviceType) {
    this.deviceType = deviceType;
    deviceTypes.put(this.serialNumber, deviceType);
  }

  @Override @NonNull public DeviceManager.DeviceType getDeviceType() {
    return this.deviceType;
  }

  private void purgeIfNecessary() {
    if (this.purgeNeeded) {
      this.purgeNeeded = false;
      this.device.purge(this.purgeWhat);
    }
  }

  @Override
  public void setBaudRate(int rate) throws RobotCoreException {
    if (!device.setBaudRate(rate)) {
      throw new RobotCoreException("FTDI driver failed to set baud rate to " + rate);
    }
  }

  @Override
  public void setDataCharacteristics(byte dataBits, byte stopBits, byte parity) throws RobotCoreException {
    if (!device.setDataCharacteristics(dataBits, stopBits, parity)) {
      throw new RobotCoreException("FTDI driver failed to set data characteristics");
    }
  }

  @Override
  public void setLatencyTimer(int latencyTimer) throws RobotCoreException {
     if (!device.setLatencyTimer((byte) latencyTimer)) {
       throw new RobotCoreException("FTDI driver failed to set latency timer to " + latencyTimer);
     }
  }

  @Override public void setBreak(boolean enable) throws RobotCoreException {
    boolean success = enable ? device.setBreakOn() : device.setBreakOff();
    if (!success) {
      throw new RobotCoreException("FTDI driver failed to set/clear break mode");
    }
  }

@Override
  public void purge(Channel channel) throws RobotCoreException {
    purgeIfNecessary();
    byte purgeByte = 0;

    switch (channel) {
      case RX: purgeByte = D2xxManager.FT_PURGE_RX; break;
      case TX: purgeByte = D2xxManager.FT_PURGE_TX; break;
      case BOTH: purgeByte = (byte)(D2xxManager.FT_PURGE_RX | D2xxManager.FT_PURGE_TX); break;
    }

    this.purgeWhat = purgeByte;
    this.purgeNeeded = true;
  }

  @Override
  public void write(byte[] data) throws RobotCoreException, NullPointerException {
    purgeIfNecessary();

    // Amazingly, there's a bug in the Android native USB layers that mean that
    // writing to an FTDI USB device is not thread-safe. So we wrap a lock around our
    // write call to guard against that.
    //
    // See:
    //      http://stackoverflow.com/questions/9644415/
    //      https://code.google.com/p/android/issues/detail?id=59467
    //      https://codereview.chromium.org/253293002/
    //
    // Note that the bug appears to be across all endpoints (though we're not completely certain
    // of that). So the lock we take is a *static* lock.

    synchronized (writeLock) {
      device.write(data);
      }
  }

  @Override
  public int read(byte[] data) throws RobotCoreException, InterruptedException, NullPointerException {
    return this.read(data, data.length, this.device.getReadTimeout());
  }

  @Override
  public int read(byte[] data, int cbToRead, int msTimeoutRemaining) throws RobotCoreException, InterruptedException, NullPointerException {
    return read(data, cbToRead, (long)msTimeoutRemaining);
  }

  @Override
  public int read(byte[] data, int cbToRead, long msTimeoutRemaining) throws RobotCoreException, InterruptedException, NullPointerException {
    purgeIfNecessary();
    while (msTimeoutRemaining > 0) {
      //
      // FT_Device.read returns one of the following
      //  -1        the device was closed
      //  -2        illegal argument: non-positive read length requested
      //  -3        incomplete initialization
      //   0        timeout reached
      //  cbToRead  read successfully completed
      //
      // While it's at it, FT_Device.read will (a) eat interrupts and (b) not terminate
      // the read when an interrupt occurs. So we better not ever actually issue a read()
      // call with a timeout of any significant duration, lest we not be able to terminate it.
      //
      final long msTimeoutQuantum = 100;
      long msTimeout = Math.min(msTimeoutQuantum, msTimeoutRemaining);
      msTimeoutRemaining -= msTimeout;
      //
      int cbRead = device.read(data, cbToRead, msTimeout);
      //
      if (cbRead == cbToRead) {
        return cbRead;
      } else if (cbRead < 0) {
        switch (cbRead) {
          case -1:
            throw new FTDeviceClosedException("error: closed: FT_Device.read()=%d", cbRead);
          case -2:
            throw new IllegalArgumentException(String.format("error: non-positive read length %d passed to FT_Device.read()", cbToRead));
          case -3:
            throw new FTDeviceIncompleteInitializationException("error: inc init: FT_Device.read()=%d", cbRead);
          default:
            throw new RobotCoreException("error: FT_Device.read()=%d", cbRead);
        }
      } else if (cbRead == 0) {
        // Poll to see if we should get out of Dodge.
        if (this.interruptRequested || Thread.currentThread().isInterrupted()) {
          throw new InterruptedException();
        }
      } else {
        throw new RobotCoreException("unexpected result %d from FT_Device_.read()", cbRead);
      }
    }
    // We hit the timeout
    return 0;
  }

  @Override
  public void requestInterrupt() {
    this.interruptRequested = true;
  }

  @Override
  public void close() {
    device.close();
    extantDevices.remove(this.serialNumber);
  }

  @Override
  public boolean isOpen() {
    return device.isOpen();
  }

  @Override
  public FirmwareVersion getFirmwareVersion() {
    return firmwareVersion;
  }

  /** Remembers the version of the firmware associated with this device for later retrieval */
  @Override
  public void setFirmwareVersion(FirmwareVersion version) {
    this.firmwareVersion = version;
  }

  @Override
  public USBIdentifiers getUsbIdentifiers() {
    USBIdentifiers result = new USBIdentifiers();
    // FT_Device ctor has this construct: // this.mDeviceInfoNode.id = this.mUsbDevice.getVendorId() << 16 | this.mUsbDevice.getProductId();
    int id = device.getDeviceInfo().id;
    result.vendorId  = (id >> 16) & 0xFFFF;
    result.productId = id & 0xFFFF;
    result.bcdDevice = device.getDeviceInfo().bcdDevice;
    return result;
  }
}
