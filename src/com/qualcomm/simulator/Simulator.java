package com.qualcomm.simulator;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import com.qualcomm.ftcrobotcontroller.opmodes.ExampleOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import net.java.games.input.*;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller.Type;

public class Simulator {

	private static OpMode opMode;
	private static ArrayList<RobotComponent> robot = new ArrayList<RobotComponent>();
	private static float robotX = 72, robotY = 72, robotRotation = 90;
	private static ArrayList<SimMotor> leftWheels = new ArrayList<SimMotor>(), rightWheels = new ArrayList<SimMotor>();

	private static final int TARGET_FPS = 60;
	private static float currentFPS = 0;

	private static Window window = new Window();
	
	private static ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment(); 
	private static Controller controller1, controller2;
	
	private enum State {
		DISABLED,
		INIT,
		ENABLED
	}

	private static State state = State.DISABLED;

	public static void main(String[] args) {
		// TODO clean up this mess
		for (Controller c : ce.getControllers()) {
			if (c.getType() == Type.GAMEPAD || c.getType() == Type.STICK) {
				if (controller1 == null) {
					controller1 = c;
				} else if (controller2 == null) {
					controller2 = c;
				}
			}
		}		
		
//		boolean a = true;
//		while (a) {
//			controller1.poll();
//			for (int i = 0; i < controller1.getComponents().length; i++) {
//				net.java.games.input.Component c = controller1.getComponents()[i];
//				System.out.println("[" + i + "] " + c.getName() + ", " + c.getIdentifier() + ": " + c.getPollData());
//			}
//			
//			try { System.in.read(); } catch (IOException e) { e.printStackTrace(); }
//		}
		
		// create window and simulation graphics
		opMode = new ExampleOpMode();
		window.repaint();
		opMode.hardwareMap = new HardwareMap();
		createRobot();
		window.refreshComponents();
		new Thread(loop()).start();
	}

	private static void createRobot() {		
		robot.add(new Frame(Color.LIGHT_GRAY, 0f, 0f, 18f, 18f, 0f, "Base"));

		robot.add(new SimMotor(-7f, 7f, 0f, "leftfront", opMode.hardwareMap));
		leftWheels.add((SimMotor) robot.get(1));
		robot.add(new SimMotor(7f, 7f, 180f, "rightfront", opMode.hardwareMap));
		rightWheels.add((SimMotor) robot.get(2));
		robot.add(new SimMotor(-7f, -7f, 0f, "leftback", opMode.hardwareMap));
		leftWheels.add((SimMotor) robot.get(3));
		robot.add(new SimMotor(7f, -7f, 180f, "rightback", opMode.hardwareMap));
		rightWheels.add((SimMotor) robot.get(4));		
		
		init();
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

					if (deltaTime >= 1000000000 / TARGET_FPS) {
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
		worldUpdate(1d / TARGET_FPS);

		updateGamepads();
		
		if (state == State.INIT) {
			opMode.init_loop();
			opMode.postInitLoop();
			start();
		} else if (state == State.ENABLED) {
			opMode.time += 1d / TARGET_FPS;
			opMode.loop();
			opMode.postLoop();
		}
				
		window.repaint();
	}

	private static void updateGamepads() {
		controller1.poll();
		opMode.gamepad1.a = controller1.getComponents()[5].getPollData() == 1f;
		opMode.gamepad1.b = controller1.getComponents()[6].getPollData() == 1f;
		opMode.gamepad1.x = controller1.getComponents()[7].getPollData() == 1f;
		opMode.gamepad1.y = controller1.getComponents()[8].getPollData() == 1f;
		opMode.gamepad1.left_bumper = controller1.getComponents()[9].getPollData() == 1f;
		opMode.gamepad1.right_bumper = controller1.getComponents()[10].getPollData() == 1f;
		opMode.gamepad1.back = controller1.getComponents()[11].getPollData() == 1f;
		opMode.gamepad1.start = controller1.getComponents()[12].getPollData() == 1f; // TODO better
		opMode.gamepad1.left_stick_button = controller1.getComponents()[13].getPollData() == 1f;
		opMode.gamepad1.right_stick_button = controller1.getComponents()[14].getPollData() == 1f;

		float hat = controller1.getComponents()[15].getPollData();
		opMode.gamepad1.dpad_left = hat == .875f || hat == 1f || hat == .125f;
		opMode.gamepad1.dpad_right = hat == .375f || hat == .5f || hat == .675f;
		opMode.gamepad1.dpad_up = hat == .125f || hat == .25f || hat == .375f;
		opMode.gamepad1.dpad_down = hat == .675f || hat == .75f || hat == .875f;
		
		opMode.gamepad1.left_stick_x = controller1.getComponent(Identifier.Axis.X).getPollData();
		opMode.gamepad1.left_stick_y = controller1.getComponent(Identifier.Axis.Y).getPollData() * -1;
		opMode.gamepad1.right_stick_x = controller1.getComponent(Identifier.Axis.RX).getPollData();
		opMode.gamepad1.right_stick_y = controller1.getComponent(Identifier.Axis.RY).getPollData() * -1;
		
		// TODO fix
		opMode.gamepad1.left_trigger = controller1.getComponent(Identifier.Axis.Z).getPollData();
		//opMode.gamepad1.right_trigger = controller1.getComponent(Identifier.Axis.RZ).getPollData();
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

		//System.out.println(leftAverage + ", " + rightAverage);
		
		// TODO Movement calculations
		robotRotation += (rightAverage - leftAverage) * 30f * timeStep;
		while (robotRotation >= 360f) robotRotation -= 360f;
		while (robotRotation < 0f) robotRotation += 360;
		
		float distance = (float) ((leftAverage + rightAverage) / (leftWheels.size() + rightWheels.size()) * 21.5f * timeStep);
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

	public static ArrayList<RobotComponent> getRobot() { return robot; }
	public static float getRobotX() { return robotX; }
	public static float getRobotY() { return robotY; }
	public static float getRobotRotation() { return robotRotation; }

}
