package com.qualcomm.simulator;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Frame implements RobotComponent {

	private final String name;
	private final Color color;
	private final float x, y, width, height, rotation;
	private final BufferedImage image;

	public Frame(final Color color, final float x, final float y, final float width, final float height, final float rotation, final String name) {
		this.name = name;

		this.color = color;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = rotation;

		image = new BufferedImage(Math.round(width * IMAGE_SCALE), Math.round(width * IMAGE_SCALE), BufferedImage.TYPE_INT_ARGB_PRE);
		final Graphics2D g = image.createGraphics();
		g.setColor(color);
		g.fillRect(0, 0, Math.round(width * IMAGE_SCALE), Math.round(height * IMAGE_SCALE));
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public Container getInfoBox() {
		return RobotComponent.createInfoBox(this);
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

}
