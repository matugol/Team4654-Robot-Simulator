package com.qualcomm.simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SimMotor implements Component, DcMotor {

	private final float x, y, rotation;

	private int currentPositon;
	private int targetPosition;
	private int maxSpeed;
	private RunMode runMode;
	private ZeroPowerBehavior zeroPowerBehavior;
	private double power = 1f;
	private Direction direction;

	private final BufferedImage image;
	private static final float width = 3, height = 1;

	public SimMotor(final float x, final float y, final float rotation, final String name, final HardwareMap map) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		map.dcMotor.put(name, this);

		image = new BufferedImage(Math.round(width * Window.SCALE), Math.round(height * Window.SCALE), BufferedImage.TYPE_INT_ARGB_PRE);
		final Graphics2D g = image.createGraphics();
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, Math.round(width * Window.SCALE), Math.round(height * Window.SCALE));
	}

	@Override
	public BufferedImage getImage() {
		return image;
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
	public int getCurrentPosition() {
		return currentPositon;
	}

	@Override
	public boolean isBusy() {
		return false;
	} // TODO

	@Override
	public int getMaxSpeed() {
		return maxSpeed;
	}

	@Override
	public void setMaxSpeed(final int encoderTicksPerSecond) {
		maxSpeed = encoderTicksPerSecond;
	}

	@Override
	public int getTargetPosition() {
		return targetPosition;
	}

	@Override
	public void setTargetPosition(final int position) {
		targetPosition = position;
	}

	@Override
	public RunMode getMode() {
		return runMode;
	}

	@Override
	public void setMode(final RunMode mode) {
		runMode = mode;
	}

	@Override
	public ZeroPowerBehavior getZeroPowerBehavior() {
		return zeroPowerBehavior;
	}

	@Override
	public void setZeroPowerBehavior(final ZeroPowerBehavior zeroPowerBehavior) {
		this.zeroPowerBehavior = zeroPowerBehavior;
	}

	@Override
	public double getPower() {
		return power;
	}

	@Override
	public void setPower(final double power) {
		this.power = power;
	}

	@Override
	public Direction getDirection() {
		return direction;
	}

	@Override
	public void setDirection(final Direction direction) {
		this.direction = direction;
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
	public DcMotorController getController() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPowerFloat() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getPowerFloat() {
		// TODO Auto-generated method stub
		return false;
	}

}
