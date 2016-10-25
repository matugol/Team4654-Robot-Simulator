package com.qualcomm.simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Frame implements RobotComponent {

	private Color color;
	private float x, y, width, height, rotation;
	private BufferedImage image;
	
	public Frame(Color color, float x, float y, float width, float height, float rotation) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		
		image = new BufferedImage(Math.round(width * Window.scale), Math.round(width * Window.scale), BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g = image.createGraphics();
		g.setColor(color);
		g.fillRect(0, 0, Math.round(width * Window.scale), Math.round(height * Window.scale));
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
	
}
