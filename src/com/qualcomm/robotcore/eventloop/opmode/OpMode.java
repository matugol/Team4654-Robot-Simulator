package com.qualcomm.robotcore.eventloop.opmode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class OpMode {

	public Gamepad gamepad1, gamepad2;
	public HardwareMap hardwareMap;
	public Telemetry telemetry;
	public double time;
	
	public void init_loop() {};
	public abstract void init();
	public void start() {};
	public abstract void loop();
	public void stop() {}
	public void updateTelemetry(Telemetry telemetry) {	/* telemetry.update(); */ }
	public void postInitLoop() { updateTelemetry(telemetry); }
	public void postLoop() { updateTelemetry(telemetry); }
	
	public OpMode() {
		gamepad1 = new Gamepad();
		gamepad2 = new Gamepad();
		// set up gamepads, hardwareMap, and telemetry
	}
	
}
