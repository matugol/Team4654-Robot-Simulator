package com.qualcomm.simulator;

import java.awt.image.BufferedImage;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

public class SimServo implements Component, Servo {

	private final float x, y, rotation;
	private float realPosition;

	private Direction direction;
	private double targetPosition;
	private double min, max;

	public SimServo(final float x, final float y, final float rotation, final String name, final HardwareMap map) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		map.servo.put(name, this);
	}

	@Override
	public BufferedImage getImage() {
		// TODO Return servo image
		return null;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public int getPortNumber() {
		return 0;
	} // TODO

	@Override
	public Direction getDirection() {
		return direction;
	}

	@Override
	public void setDirection(final Direction direction) {
		this.direction = direction;
	}

	@Override
	public double getPosition() {
		return targetPosition;
	}

	@Override
	public void setPosition(final double position) {
		targetPosition = position;
	}

	@Override
	public void scaleRange(final double min, final double max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public Manufacturer getManufacturer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDeviceName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConnectionInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void resetDeviceConfigurationForOpMode() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public ServoController getController() {
		// TODO Auto-generated method stub
		return null;
	}

}
