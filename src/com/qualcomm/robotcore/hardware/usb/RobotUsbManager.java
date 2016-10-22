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

package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.util.SerialNumber;

/**
 * USB Manager Interface
 */
public interface RobotUsbManager {

	/**
	 * Scan for USB devices and return the number of devices found
	 *
	 * @return the number of devices in the device info list
	 */
	int scanForDevices() throws RobotCoreException;

	/**
	 * This is a hack. What's up is that ModernRoboticsUsbUtil.openUsbDevice internally calls {@link #scanForDevices()}, which
	 * totally isn't necessary inside of HardwareDeviceManager.scanForDevices,
	 * since it *just did that*. And this is an expensive call, which adds up for each and every
	 * USB device we open. By calling this method, scanForDevices() will become a no-op, not actually
	 * re-executing the scan.
	 *
	 * In point of fact, this may not strictly be necessary. It might be enough that having
	 * RobotUsbManager be thread-safe is enough (it wasn't previously). But we haven't tested
	 * that compromise, and the hack here isn't a large one, so we live with it. And it's a
	 * performance win, if nothing else.
	 *
	 * @see #thawScanForDevices()
	 */
	void freezeScanForDevices();

	/**
	 * Undoes the work of {@link #freezeScanForDevices()}.
	 *
	 * @see #freezeScanForDevices()
	 */
	void thawScanForDevices();

	/**
	 * get device serial number
	 * 
	 * @param index index of device
	 * @return serial number
	 * @throws RobotCoreException
	 */
	SerialNumber getDeviceSerialNumberByIndex(int index) throws RobotCoreException;

	/**
	 * get device description
	 * 
	 * @param index index of device
	 * @return description
	 * @throws RobotCoreException
	 */
	String getDeviceDescriptionByIndex(int index) throws RobotCoreException;

	/**
	 * Open device by serial number
	 * 
	 * @param serialNumber USB serial number
	 * @return usb device
	 * @throws RobotCoreException
	 */
	RobotUsbDevice openBySerialNumber(SerialNumber serialNumber) throws RobotCoreException;
}
