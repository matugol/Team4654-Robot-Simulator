package com.qualcomm.simulator;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SimMotor implements RobotComponent, DcMotor {

	private final String name;
	private final float x, y, rotation;

	private int currentPositon;
	private int targetPosition;
	private int maxSpeed;
	private RunMode runMode;
	private ZeroPowerBehavior zeroPowerBehavior;
	private double power = 0f;
	private Direction direction;

	private final BufferedImage image;
	private static final float width = 3 * IMAGE_SCALE, height = IMAGE_SCALE;

	public SimMotor(final float x, final float y, final float rotation, final String name, final HardwareMap map) {
		this.name = name;

		map.dcMotor.put(name, this);
		this.x = x;
		this.y = y;
		this.rotation = rotation;

		image = new BufferedImage(Math.round(width), Math.round(height), BufferedImage.TYPE_INT_ARGB_PRE);
		final Graphics2D g = image.createGraphics();
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, Math.round(width), Math.round(height));
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public Container getInfoBox() {
		return RobotComponent.createInfoBox(this, String.format("Power: %.2f", power));
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}

	@Override
	public String getName() {
		return name;
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
		return power * (direction == Direction.FORWARD ? 1 : -1);
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

}
