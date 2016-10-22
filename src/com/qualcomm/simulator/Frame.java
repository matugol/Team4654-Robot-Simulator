package com.qualcomm.simulator;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Frame implements Component {

	private final Color color;
	private final float x, y, width, height, rotation;
	private final BufferedImage image;

	public Frame(final Color color, final float x, final float y, final float width, final float height, final float rotation) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = rotation;

		image = new BufferedImage(Math.round(width * Window.SCALE), Math.round(width * Window.SCALE), BufferedImage.TYPE_INT_ARGB_PRE);
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
