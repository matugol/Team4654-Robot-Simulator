package com.qualcomm.simulator;

public class Simulator {

//	private static OpMode opMode;
	
	private static int targetFPS = 60;
	private static int currentFPS = 0;
	
	public static void main(String[] args) {
//		opMode.init();
		
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
						update(deltaTime);
						
						currentTime = System.nanoTime();
						deltaSecond = currentTime - previousSecond;
						
						if (deltaSecond >= 1000000000) {
							setCurrentFPS((int) (fpsCount / (deltaSecond / 1000000000)));
							previousSecond = currentTime;
							fpsCount = 0;
						}
					}
				}
			}
		};
	}

	private static void update(long deltatime) {
		// do world update
		
//		opMode.loop();
	}
	
	public static void setCurrentFPS(int fps) {
		currentFPS = fps;
	}
	
}
