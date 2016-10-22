/*
 * Copyright (c) 2015 Qualcomm Technologies Inc
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

public class LED implements HardwareDevice {

	private DigitalChannelController controller = null;
	private int physicalPort = -1;

	/***
	 * Constructor
	 * 
	 * @param controller Digital Channel Controller this LED is attached to
	 * @param physicalPort the physical port it's plugged into.
	 */
	public LED(final DigitalChannelController controller, final int physicalPort) {
		this.controller = controller;
		this.physicalPort = physicalPort;

		controller.setDigitalChannelMode(physicalPort, DigitalChannelController.Mode.OUTPUT);
	}

	/**
	 * A method to turn on or turn off the LED
	 * 
	 * @param set - true turns it on, false turns it off.
	 */
	public void enable(final boolean set) {
		controller.setDigitalChannelState(physicalPort, set);
	}

	@Override
	public Manufacturer getManufacturer() {
		return controller.getManufacturer();
	}

	@Override
	public String getDeviceName() {
		return "LED";
	}

	@Override
	public String getConnectionInfo() {
		return String.format("%s; port %d", controller.getConnectionInfo(), physicalPort);
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void resetDeviceConfigurationForOpMode() {}

	@Override
	public void close() {

	}
}
