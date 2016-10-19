package com.qualcomm.robotcore.hardware;

public interface DcMotor {

	public enum RunMode {
		RUN_WITHOUT_ENCODER,
		RUN_TO_POSITION,
		RUN_USING_ENCODER,
		STOP_AND_RESET_ENCODER
	}
	
	public enum ZeroPowerBehavior {
		BRAKE,
		FLOAT,
		UNKNOWN
	}
	
	public enum Direction {
		FORWARD,
		REVERSE
	}
	
	//public DcMotorController getController();
	public int getCurrentPosition();
	public int getMaxSpeed();
	public RunMode getMode();
	public int getPortNumber();
	public int getTargetPosition();
	public ZeroPowerBehavior getZeroPowerBehavior();
	public boolean isBusy();
	public void setMaxSpeed(int encoderTicksPerSecond);
	public void setMode(RunMode mode);
	public void	setTargetPosition(int position);
	public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior);
	
	public double getPower();
	public void setPower(double power);
	public Direction getDirection();
	public void setDirection(Direction direction);
	
}
