/*
 * Copyright (c) 2015 Qualcomm Technologies Inc
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
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.robotcore.hardware.usb.ftdi;

import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;

public class RobotUsbManagerFtdi implements RobotUsbManager {

	private final Context context;
	private D2xxManager d2xxManager;
	private int numberOfDevices;
	private boolean scanIsLocked = false;

	/**
	 * Constructor
	 * 
	 * @param context application context
	 */
	public RobotUsbManagerFtdi(final Context context) throws RobotCoreException {
		this.context = context;

		try {
			d2xxManager = D2xxManager.getInstance(context);
		} catch (final D2xxManager.D2xxException e) {
			RobotLog.e("Unable to create D2xxManager; cannot open USB devices");
			RobotLog.logStacktrace(e);
			throw RobotCoreException.createChained(e, "unable to create D2xxManager");
		}
	}

	/**
	 * Scan for USB devices
	 * 
	 * @return number of USB devices found
	 * @throws RobotCoreException
	 */
	@Override
	public synchronized int scanForDevices() throws RobotCoreException {
		if (!scanIsLocked) {
			numberOfDevices = d2xxManager.createDeviceInfoList(context);
		}
		return numberOfDevices;
	}

	@Override
	synchronized public void freezeScanForDevices() {
		scanIsLocked = true;
	}

	@Override
	synchronized public void thawScanForDevices() {
		scanIsLocked = false;
	}

	/**
	 * get device serial number. Is thread safe.
	 * 
	 * @param index index of device
	 * @return serial number
	 * @throws RobotCoreException
	 */
	@Override
	public SerialNumber getDeviceSerialNumberByIndex(final int index) throws RobotCoreException {
		return new SerialNumber(d2xxManager.getDeviceInfoListDetail(index).serialNumber);
	}

	// Is thread safe
	@Override
	public String getDeviceDescriptionByIndex(final int index) throws RobotCoreException {
		return d2xxManager.getDeviceInfoListDetail(index).description;
	}

	/**
	 * Open device by serial number. Is threadsafe since we only ever pass the one context.
	 * 
	 * @param serialNumber USB serial number
	 * @return usb device
	 * @throws RobotCoreException
	 */
	@Override
	public RobotUsbDevice openBySerialNumber(final SerialNumber serialNumber) throws RobotCoreException {
		final FT_Device device = d2xxManager.openBySerialNumber(context, serialNumber.toString());
		if (device == null) {
			throw new RobotCoreException("FTDI driver failed to open USB device with serial number " + serialNumber + " (returned null device)");
		}

		return new RobotUsbDeviceFtdi(device, serialNumber);
	}
}
