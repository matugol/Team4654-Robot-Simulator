package com.qualcomm.simulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public interface RobotComponent {
	
	public Container getInfoBox();
	public BufferedImage getImage();
	
	public String getName();
	
	public float getX();
	public float getY();
	public float getRotation();
	
	public static Container createInfoBox(RobotComponent comp, String... values) {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		panel.add(new JLabel(String.format("%s (%s)", comp.getName(), comp.getClass().getSimpleName())));
		for (String value : values) {
			panel.add(new JLabel(" " + value)).setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		}
		JPanel ret = new JPanel(new BorderLayout());
		ret.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		ret.add(panel);
		return ret;
	}
	
}
