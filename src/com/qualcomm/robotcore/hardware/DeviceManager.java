/*
 * Copyright (c) 2014, 2015 Qualcomm Technologies Inc
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.robotcore.hardware;

import android.support.annotation.Nullable;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.configuration.UserSensorType;
import com.qualcomm.robotcore.util.SerialNumber;

import java.util.Map;

@SuppressWarnings("javadoc")
public interface DeviceManager {

  /**
   * Enum of known USB Device Types (Note that the MR .AAR library has an internal copy of this)
   */
  enum DeviceType {
    FTDI_USB_UNKNOWN_DEVICE,
    MODERN_ROBOTICS_USB_UNKNOWN_DEVICE,
    MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER,
    MODERN_ROBOTICS_USB_SERVO_CONTROLLER,
    MODERN_ROBOTICS_USB_LEGACY_MODULE,
    MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE,
    MODERN_ROBOTICS_USB_SENSOR_MUX,   // does this really exist? probably not
    UNKNOWN_DEVICE
  }

  /**
   * Get a listing of all Modern Robotics devices connected.
   * <p>
   * This method will attempt to open all USB devices that are using an FTDI USB chipset. It will
   * then probe the device to determine if it is a Modern Robotics device. Finally, it will close the
   * device.
   * <p>
   * Because of the opening and closing of devices, it is recommended that this method is not called
   * while any FTDI devices are in use.
   *
   * @return a map of serial numbers to Modern Robotics device types
   * @throws RobotCoreException if unable to open a device
   */
  Map<SerialNumber, DeviceType> scanForUsbDevices() throws RobotCoreException;

  /**
   * Create an instance of a DcMotorController
   *
   * @param serialNumber serial number of controller
   * @return an instance of a DcMotorController
   * @throws RobotCoreException if unable to create instance
   * @throws InterruptedException
   */
  DcMotorController createUsbDcMotorController(SerialNumber serialNumber)
      throws RobotCoreException, InterruptedException;

  /**
   * Create an instance of a DcMotor
   *
   * @param controller DC Motor controller this motor is attached to
   * @param portNumber physical port number on the controller
   * @return an instance of a DcMotor
   */
  DcMotor createDcMotor(DcMotorController controller, int portNumber);
  DcMotor createDcMotorEx(DcMotorController controller, int portNumber);

  /**
   * Create an instance of a ServoController
   *
   * @param serialNumber serial number of controller
   * @return an instance of a ServoController
   * @throws RobotCoreException if unable to create instance
   * @throws InterruptedException
   */
  ServoController createUsbServoController(SerialNumber serialNumber)
      throws RobotCoreException, InterruptedException;

  /**
   * Create an instance of a Servo
   *
   * @param controller Servo controller this servo is attached to
   * @param portNumber physical port number on the controller
   * @return an instance of a Servo
   */
  Servo createServo(ServoController controller, int portNumber);
  Servo createServoEx(ServoController controller, int portNumber);

  CRServo createCRServo(ServoController controller, int portNumber);

  /**
   * Create an instance of a LegacyModule
   *
   * @param serialNumber serial number of legacy module
   * @return an instance of a LegacyModule
   * @throws RobotCoreException if unable to create instance
   * @throws InterruptedException
   */
  LegacyModule createUsbLegacyModule(SerialNumber serialNumber)
      throws RobotCoreException, InterruptedException;

  /**
   *
   * @param serialNumber serial number of Core Device Interface module
   * @return - an instance of a Core Device Interface Module
   * @throws RobotCoreException if unable to create instance
   * @throws InterruptedException
   */
  DeviceInterfaceModule createDeviceInterfaceModule(SerialNumber serialNumber)
      throws RobotCoreException, InterruptedException;

  /**
   *
   * @param legacyModule the Legacy Module this sensor is attached to
   * @param physicalPort port number on Legacy Module it's connected to
   * @return an instance of the NXT Touch Sensor
   */
  TouchSensor createNxtTouchSensor(LegacyModule legacyModule, int physicalPort);

  /**
   *
   * @param legacyModule the Legacy Module this sensor is attached to
   * @param port port number on Legacy Module this sensor is connected to.
   * @return an instance of the NXT Touch Sensor Multiplexer
   */
  TouchSensorMultiplexer createNxtTouchSensorMultiplexer(LegacyModule legacyModule, int port);

  /**
   *
   * @param controller Analog Input Controller Module this device is connected to
   * @return - an instance of an Analog Input device
   */
  AnalogInput createAnalogInputDevice(AnalogInputController controller, int channel);

  /**
   *
   * @param controller Analog Output Controller Module this device is connected to
   * @return - an instance of an Analog Output device
   */
  AnalogOutput createAnalogOutputDevice(AnalogOutputController controller, int channel);

  /**
   *
   * @param controller Device Interface Module this device is connected to
   * @return - an instance of an Digital Channel device
   */
  DigitalChannel createDigitalChannelDevice(DigitalChannelController controller, int channel);

  /**
   *
   * @param controller PWM Output Controller Module this device is connected to
   * @return - an instance of an Digital Channel device
   */
  PWMOutput createPwmOutputDevice(PWMOutputController controller, int channel);
  PWMOutput createPwmOutputDeviceEx(PWMOutputController controller, int channel);

  /**
   *
   * @param controller I2c Controller Module this device is conneced to
   * @param channel the channel it's connected to on the Controller
   * @return an instance of an I2c Channel device
   */
  I2cDevice createI2cDevice(I2cController controller, int channel);


  /**
   * Returns a new instance of a user-defined sensor type.
   */
  @Nullable HardwareDevice createUserI2cDevice(I2cController controller, int channel, UserSensorType type);

  /**
   * Create an instance of an NXT DcMotorController
   * @param legacyModule Legacy Module this device is connected to
   * @param physicalPort port number on the Legacy Module this device is connected to
   * @return a DcMotorController
   */
  DcMotorController createNxtDcMotorController(LegacyModule legacyModule, int physicalPort);

  /**
   * Create an instance of an NXT ServoController
   * @param legacyModule Legacy Module this device is connected to
   * @param physicalPort port number on the Legacy Module this device is connected to
   * @return a ServoController
   */
  ServoController createNxtServoController(LegacyModule legacyModule, int physicalPort);

  /**
   * Create an instance of a NxtCompassSensor
   * @param legacyModule Legacy Module this device is connected to
   * @param physicalPort port number on the Legacy Module this device is connected to
   * @return a CompassSensor
   */
  CompassSensor createNxtCompassSensor(LegacyModule legacyModule, int physicalPort);

  /**
   * Create an instance of a TouchSensor
   * @param deviceInterfaceModule Legacy Module this device is connected to
   * @param physicalPort port number on the Device Interface Module this device is connected to
   * @return a TouchSensor
   */
  TouchSensor createDigitalTouchSensor(DeviceInterfaceModule deviceInterfaceModule, int physicalPort);

  /**
   * Create an instance of a AccelerationSensor
   * @param legacyModule Legacy Module this device is connected to
   * @param physicalPort port number on the Legacy Module this device is connected to
   * @return an AccelerationSensor
   */
  AccelerationSensor createNxtAccelerationSensor(LegacyModule legacyModule, int physicalPort);

  /**
   * Create an instance of a LightSensor
   * @param legacyModule Legacy Module this device is connected to
   * @param physicalPort port number on the Legacy Module this device is connected to
   * @return a LightSensor
   */
  LightSensor createNxtLightSensor(LegacyModule legacyModule, int physicalPort);

  /**
   * Create an instance of a IrSeekerSensor
   * @param legacyModule Legacy Module this device is connected to
   * @param physicalPort port number on the Legacy Module this device is connected to
   * @return an IrSeekerSensor
   */
  IrSeekerSensor createNxtIrSeekerSensor(LegacyModule legacyModule, int physicalPort);

  /**
   * Create an instance of a IrSeekerSensorV3
   * @param i2cController the {@link I2cController} this device is connected to
   * @param physicalPort port number on the Device Interface Module this device is connected to
   * @return an IrSeekerSensor
   */
  IrSeekerSensor createI2cIrSeekerSensorV3(I2cController i2cController, int physicalPort);

  /**
   * Create an instance of an UltrasonicSensor
   * @param legacyModule Legacy Module this device is connected to
   * @param physicalPort port number on the Legacy Module this device is connected to
   * @return an UltrasonicSensor
   */
  UltrasonicSensor createNxtUltrasonicSensor(LegacyModule legacyModule, int physicalPort);

  /**
   * Create an instance of a GyroSensor
   * @param legacyModule Legacy Module this device is connected to
   * @param physicalPort port number on the Legacy Module this device is connected to
   * @return a GyroSensor
   */
  GyroSensor createNxtGyroSensor(LegacyModule legacyModule, int physicalPort);

  /**
   * Create an instance of a GyroSensor
   * @param i2cController   module this device is connected to
   * @param physicalPort    port number on the Legacy Module this device is connected to
   * @return a GyroSensor
   */
  GyroSensor createModernRoboticsI2cGyroSensor(I2cController i2cController, int physicalPort);

  /**
   * Create an instance of an OpticalDistanceSensor
   * @param analogInputController controller this sensor is connected to
   * @param physicalPort the port number on the controller it's plugged into
   * @return an OpticalDistanceSensor
   */
  OpticalDistanceSensor createAnalogOpticalDistanceSensor(AnalogInputController analogInputController, int physicalPort);

  /**
   * Create an instance of a ColorSensor
   * @param controller Device Interface Module this sensor is connected to
   * @param channel the I2C port on the Device Interface this module is connected to
   * @return a ColorSensor
   */
  ColorSensor createAdafruitI2cColorSensor(I2cController controller, int channel);

  /**
   * Create an instance of a ColorSensor
   * @param controller Legacy Module this sensor is attached to
   * @param channel the I2C port it's connected to
   * @return a ColorSensor
   */
  ColorSensor createNxtColorSensor(LegacyModule controller, int channel);

  /**
   * Create an instance of a ColorSensor
   * @param controller Device Interface Module this sensor is attached to
   * @param channel the I2C port it's connected to
   * @return a ColorSensor
   */
  ColorSensor createModernRoboticsI2cColorSensor(I2cController controller, int channel);

  /**
   * Create an instance of an LED
   * @param controller Digital Channel Controller this LED is connected to
   * @param channel the digital port it's connected to
   * @return an LED
   */
  LED createLED(DigitalChannelController controller, int channel);
}
