package com.qualcomm.simulator;

import java.awt.image.BufferedImage;

public interface Component {
	
	public BufferedImage getImage();
	
	public float getX();
	public float getY();
	public float getRotation();
	
}
