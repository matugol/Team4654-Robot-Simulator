package com.qualcomm.simulator;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class SimServo implements RobotComponent, Servo {

	private String name;
	private float x, y, rotation;
	private float realPosition;
	
	private Direction direction;
	private double targetPosition;
	private double min, max;
	
	public SimServo(float x, float y, float rotation, String name, HardwareMap map) {
		this.name = name;
		map.servo.put(name, this);
		
		this.x = x;
		this.y = y;
		this.rotation = rotation;	
	}
	
	@Override
	public String toString() { return name; }
	
	@Override
	public Container getInfoBox() {
		return RobotComponent.createInfoBox(this, String.format("Position: %.2f", this.realPosition));
	}
	
	@Override
	public BufferedImage getImage() {
		// TODO Return servo image
		return null;
	}

	@Override
	public String getName() {
		return this.name;
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
	public Direction getDirection() { return direction; }
	@Override
	public void setDirection(Direction direction) { this.direction = direction; }
	@Override
	public double getPosition() { return targetPosition; }
	@Override
	public void setPosition(double position) { targetPosition = position; }
	@Override
	public void scaleRange(double min, double max) { this.min = min; this.max = max; }
	
}
