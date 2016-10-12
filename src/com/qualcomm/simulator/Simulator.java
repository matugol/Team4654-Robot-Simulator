package com.qualcomm.simulator;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Simulator {

	private static OpMode opMode;
	
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
		
		new Thread(loop()).start();
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
		// do world update
		
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
