package com.qualcomm.simulator;

import java.awt.image.BufferedImage;

public interface RobotComponent {
	
	public BufferedImage getImage();
	
	public float getX();
	public float getY();
	public float getRotation();
	
}
