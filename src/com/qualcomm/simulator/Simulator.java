package com.qualcomm.simulator;

import java.awt.Color;
import java.util.ArrayList;

import com.qualcomm.ftcrobotcontroller.opmodes.ExampleOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.sun.java.swing.plaf.windows.resources.windows;

public class Simulator {

	private static OpMode opMode;
	private static ArrayList<Component> robot = new ArrayList<Component>();
	private static float robotX = 72, robotY = 72, robotRotation = 90;
	private static ArrayList<SimMotor> leftWheels = new ArrayList<SimMotor>(), rightWheels = new ArrayList<SimMotor>();

	private static int targetFPS = 60;
	private static float currentFPS = 0;

	private static Window window = new Window();

	private enum State {
		DISABLED,
		INIT,
		ENABLED
	}

	private static State state = State.DISABLED;

	public static void main(String[] args) {
		// create window and simulation graphics
		opMode = new ExampleOpMode();
		window.repaint();
		opMode.hardwareMap = new HardwareMap();
		createRobot();
		new Thread(loop()).start();
	}

	private static void createRobot() {		
		robot.add(new Frame(Color.LIGHT_GRAY, 0f, 0f, 18f, 18f, 0f));

		robot.add(new SimMotor(-7f, 7f, 0f, "leftfront", opMode.hardwareMap));
		leftWheels.add((SimMotor) robot.get(1));
		robot.add(new SimMotor(7f, 7f, 180f, "rightfront", opMode.hardwareMap));
		rightWheels.add((SimMotor) robot.get(2));
		robot.add(new SimMotor(-7f, -7f, 0f, "leftback", opMode.hardwareMap));
		leftWheels.add((SimMotor) robot.get(3));
		robot.add(new SimMotor(7f, -7f, 180f, "rightback", opMode.hardwareMap));
		rightWheels.add((SimMotor) robot.get(4));		
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
		worldUpdate(1d / targetFPS);

		// update gamepads

		if (state == State.INIT) {
			opMode.init_loop();
			opMode.postInitLoop();
		} else if (state == State.ENABLED) {
			opMode.time += 1d / targetFPS;
			opMode.loop();
			opMode.postLoop();
		}
		
		robotRotation += 0.42f;
		
		window.repaint();
	}

	private static void worldUpdate(double timeStep) {
		float leftAverage = 0f;
		for (SimMotor motor : leftWheels) {
			leftAverage += motor.getPower();
		}
		//leftAverage /= leftWheels.size();
		float rightAverage = 0f;
		for (SimMotor motor : rightWheels) {
			rightAverage += motor.getPower();
		}
		//rightAverage /= rightWheels.size();

		// TODO Movement calculations
		robotRotation += (leftAverage - rightAverage) * 5.4f;
		if (robotRotation >= 360f) robotRotation -= 360f;
		if (robotRotation < 0f) robotRotation += 360;
		
		float distance = (float) ((leftAverage + rightAverage) / (leftWheels.size() + rightWheels.size()) * 5f * timeStep);
		robotX += (float) (distance * Math.cos(Math.toRadians(robotRotation)));
		robotY -= (float) (distance * Math.sin(Math.toRadians(robotRotation)));
		
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

	public static ArrayList<Component> getRobot() { return robot; }
	public static float getRobotX() { return robotX; }
	public static float getRobotY() { return robotY; }
	public static float getRobotRotation() { return robotRotation; }

}
