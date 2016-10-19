package com.qualcomm.simulator;

import java.awt.image.BufferedImage;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor.Direction;

public class SimMotor implements Component, DcMotor {

	private float x, y, rotation;
	
	private int currentPositon;
	private int targetPosition;
	private int maxSpeed;
	private RunMode runMode;
	private ZeroPowerBehavior zeroPowerBehavior;
	private double power;
	private Direction direction;
	
	public SimMotor(float x, float y, float rotation, String name, HardwareMap map) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		map.dcMotor.put(name, this);
	}
	
	@Override
	public BufferedImage getImage() {
		// TODO Return motor image
		return null;
	}
	
	@Override
	public float getX() { return x; }
	@Override
	public float getY() { return y; }
	@Override
	public float getRotation() { return rotation; }
	@Override
	public int getPortNumber() { return 0; } // TODO
	@Override
	public int getCurrentPosition() { return currentPositon; }
	@Override
	public boolean isBusy() { return false; } // TODO
	@Override
	public int getMaxSpeed() { return maxSpeed; }
	@Override
	public void setMaxSpeed(int encoderTicksPerSecond) { maxSpeed = encoderTicksPerSecond; }
	@Override
	public int getTargetPosition() { return targetPosition; }
	@Override
	public void setTargetPosition(int position) { targetPosition = position; }
	@Override
	public RunMode getMode() { return runMode; }
	@Override
	public void setMode(RunMode mode) { runMode = mode; }
	@Override
	public ZeroPowerBehavior getZeroPowerBehavior() { return zeroPowerBehavior; }
	@Override
	public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) { this.zeroPowerBehavior = zeroPowerBehavior; }

	@Override
	public double getPower() { return power; }
	@Override
	public void setPower(double power) { this.power = power; }
	@Override
	public Direction getDirection() { return direction; }
	@Override
	public void setDirection(Direction direction) { this.direction = direction; }
	
}
