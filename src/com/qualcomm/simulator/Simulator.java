package com.qualcomm.simulator;

import java.util.ArrayList;

import com.qualcomm.ftcrobotcontroller.opmodes.ExampleOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Simulator {

	private static OpMode opMode;
	private static ArrayList<Component> robot;
	float robotX = 0, robotY = 0, robotRotation = 0;
	private static ArrayList<SimMotor> leftWheels, rightWheels;
	
	private static int targetFPS = 60;
	private static float currentFPS = 0;
	
	private enum State {
		DISABLED,
		INIT,
		ENABLED
	}
	
	private static State state = State.DISABLED;
	
	public static void main(String[] args) {
		// create window and simulation graphics
		opMode = new ExampleOpMode();
		createRobot();
		new Thread(loop()).start();
	}
	
	private static void createRobot() {
		robot.add(new SimMotor(-.5f, .7f, 0f, "leftfront", opMode.hardwareMap));
		leftWheels.add((SimMotor) robot.get(0));
		robot.add(new SimMotor(.5f, .7f, 0f, "rightfront", opMode.hardwareMap));
		rightWheels.add((SimMotor) robot.get(1));
		robot.add(new SimMotor(-.5f, -.7f, 0f, "leftback", opMode.hardwareMap));
		leftWheels.add((SimMotor) robot.get(2));
		robot.add(new SimMotor(.5f, -.7f, 0f, "rightback", opMode.hardwareMap));
		rightWheels.add((SimMotor) robot.get(3));
	}
	
	private static Runnable loop() {
		return new Runnable() {

			@Override
			public void run() {			
				long deltaTime, currentTime, previousTime = System.nanoTime(), deltaSecond, previousSecond = System.nanoTime();
				int fpsCount = 0;
				
				while (true) {
					currentTime = System.nanoTime();
					deltaTime = currentTime - previousTime;
					
					if (deltaTime >= 1000000000 / targetFPS) {
						previousTime = currentTime;
						fpsCount++;
						fixedUpdate();
						
						currentTime = System.nanoTime();
						deltaSecond = currentTime - previousSecond;
						
						if (deltaSecond >= 1000000000) {
							setCurrentFPS(fpsCount / (deltaSecond / 1000000000));
							previousSecond = currentTime;
							fpsCount = 0;
						}
					}
				}
			}
		};
	}

	private static void fixedUpdate() { // Update always advances (1 / targetFPS) of a second
		worldUpdate();
		
		// update gamepads
		
		if (state == State.INIT) {
			opMode.init_loop();
			opMode.postInitLoop();
		} else if (state == State.ENABLED) {
			opMode.time += 1d / targetFPS;
			opMode.loop();
			opMode.postLoop();
		}
	}
	
	private static void worldUpdate() {
		float leftAverage = 0f;
		for (SimMotor motor : leftWheels) {
			leftAverage += motor.getPower();
		}
		leftAverage /= leftWheels.size();
		float rightAverage = 0f;
		for (SimMotor motor : rightWheels) {
			rightAverage += motor.getPower();
		}
		rightAverage /= rightWheels.size();
		
		// TODO Movement calculations
	}
	
	public static void setCurrentFPS(float fps) { currentFPS = fps; }
	public static float getCurrentFPS() { return currentFPS; }
	
	public static State getState() { return state; }
	
	public static boolean init() {
		if (state == State.DISABLED) {
			state = State.INIT;
			opMode.init();
			return true;
		}
		
		return false;
	}
	
	public static boolean start() {
		if (state == State.INIT) {
			state = State.ENABLED;
			opMode.start();
			return true;
		}
		
		return false;
	}
	
	public static boolean stop() {
		if (state != State.DISABLED) {
			state = State.DISABLED;
			opMode.stop();
			return true;
		}
		
		return false;
	}
	
}
