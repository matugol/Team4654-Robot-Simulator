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

package com.qualcomm.robotcore.hardware.usb;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.util.SerialNumber;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * USB Device Interface
 */
public interface RobotUsbDevice {

  public enum Channel { RX, TX, NONE, BOTH };

  public class FirmwareVersion {
    public int  majorVersion;
    public int  minorVersion;

    public FirmwareVersion() {
      this(0);
    }
    public FirmwareVersion(int bVersion) {
      majorVersion = (bVersion >> 4) & 0x0F;
      minorVersion = (bVersion >> 0) & 0x0F;
    }

    @Override public String toString() {
      return String.format("v%d.%d", this.majorVersion, this.minorVersion);
    }
  }

  /** @see #getUsbIdentifiers() */
  public class USBIdentifiers {
    public int    vendorId;
    public int    productId;
    public int    bcdDevice;

    private static final int vendorIdFTDI = 0x0403;
    private static final Set<Integer> productIdsModernRobotics = new HashSet<Integer>(Arrays.asList(new Integer[] { 0x6001 }));
    private static final Set<Integer> bcdDevicesModernRobotics = new HashSet<Integer>(Arrays.asList(new Integer[] { 0x0600 }));

    public boolean isModernRoboticsDevice() {
      return this.vendorId == vendorIdFTDI
              && productIdsModernRobotics.contains(this.productId)
              && bcdDevicesModernRobotics.contains(this.bcdDevice & 0xFF00);
    }

  }


  /**
   * Set the baud rate
   * @param rate baud rate
   * @throws RobotCoreException
   */
  public void setBaudRate(int rate) throws RobotCoreException;

  /**
   * Set the Data Characteristics
   * @param dataBits data bits
   * @param stopBits stop bits
   * @param parity   parity
   * @throws RobotCoreException
   */
  public void setDataCharacteristics(byte dataBits, byte stopBits, byte parity) throws RobotCoreException;

  /**
   * Set the latency timer
   * @param latencyTimer latency timer
   * @throws RobotCoreException
   */
  public void setLatencyTimer(int latencyTimer) throws RobotCoreException;


  void setBreak(boolean enable) throws RobotCoreException;

  /**
   * Purge the buffers
   * @param channel RX, TX, etc
   * @throws RobotCoreException
   */
  public void purge(Channel channel) throws RobotCoreException;

  /**
   * Write to device
   * @param data byte array
   * @return the number of bytes actually written
   * @throws RobotCoreException
   */
  public void write(byte[] data) throws RobotCoreException, NullPointerException;

  /**
   * Read from device
   * @param data byte array
   * @return number of bytes read into byte array
   * @throws RobotCoreException
   */
  public int read(byte[] data) throws RobotCoreException, InterruptedException, NullPointerException;

  /**
   * Read from device
   * @param data byte array
   * @param length length
   * @param msTimeout timeout
   * @return number of bytes read into byte array, or 0 if the timeout was hit
   * @throws RobotCoreException
   * @throws NullPointerException thrown on usb disconnect
   */
  public int read(byte[] data, int length, int msTimeout) throws RobotCoreException, InterruptedException, NullPointerException;
  public int read(byte[] data, int length, long msTimeout) throws RobotCoreException, InterruptedException, NullPointerException;

  /**
   * Works around a bug wherein FT_Device.read() will eat interrupts silently without terminating
   * the read call. Will cause pending read()s to throw an InterruptedException
   */
  void requestInterrupt();

  /**
   * Closes the device
   */
  void close();

  /**
   * Returns whether the device is open or not
   */
  boolean isOpen();

  /** Returns the firmware version of this USB device, or null if no such version is known.
   * @see #setFirmwareVersion(FirmwareVersion) */
  public FirmwareVersion getFirmwareVersion();

  /** Sets the firmware version of this USB device.
   * @see #getFirmwareVersion() */
  public void setFirmwareVersion(FirmwareVersion version);

  /**
   * Returns the USB-level vendor and product id of this device.
   *
   * All the devices we are interested in use FTDI chips, which report as vendor 0x0403.
   * Modern Robotics modules (currently?) use a product id of 0x6001 and bcdDevice of 0x0600.
   * Note that for FTDI,
   * only the upper byte of the two-byte bcdDevice seems to be of significance.
   *
   * "Every Universal Serial Bus (USB) device must be able to provide a single device descriptor that
   * contains relevant information about the device. The USB_DEVICE_DESCRIPTOR structure describes a
   * device descriptor. Windows uses that information to derive various sets of information. For
   * example, the idVendor and idProduct fields specify vendor and product identifiers, respectively.
   * Windows uses those field values to construct a hardware ID for the device. To view the hardware
   * ID of a particular device, open Device Manager and view device properties. In the Details tab,
   * the Hardware Ids property value indicates the hardware ID ("USB\XXX") that is generated by Windows.
   * The bcdUSB field indicates the version of the USB specification to which the device conforms.
   * For example, 0x0200 indicates that the device is designed as per the USB 2.0 specification. The
   * bcdDevice value indicates the device-defined revision number. The USB driver stack uses bcdDevice,
   * along with idVendor and idProduct, to generate hardware and compatible IDs for the device. You
   * can view the those identifiers in Device Manager. The device descriptor also indicates the total
   * number of configurations that the device supports"
   *
   * @see <a href="https://msdn.microsoft.com/en-us/library/windows/hardware/ff539283(v=vs.85).aspx">USB Device Descriptors</a>
   */
  public USBIdentifiers getUsbIdentifiers();

  @NonNull SerialNumber getSerialNumber();

  void setDeviceType(@NonNull DeviceManager.DeviceType deviceType);
  @NonNull DeviceManager.DeviceType getDeviceType();
}
