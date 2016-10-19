package com.qualcomm.robotcore.hardware;

public interface Servo {

	public enum Direction {
		FORWARD,
		REVERSE
	}
	
	//public ServoController getController();
	public Direction getDirection();
	public int getPortNumber();
	public double getPosition();
	public void scaleRange(double min, double max);
	public void setDirection(Direction direction);
	public void setPosition(double position);
	
}
