package com.qualcomm.simulator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {

	public static final int scale = 5; // pixels per inch
	private static World world;
	
	public Window() {
		super("Team4654 Robot Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		world = new World();
		add(world);
		
		//setIgnoreRepaint(true);
		//setResizable(false);
		//pack();
		setSize(new Dimension(800, 800));
		setVisible(true);
	}
	
	private class World extends JPanel {
	
		public void paint(Graphics g) {	  
			Graphics2D gg = (Graphics2D) g;
		    BufferedImage robot = new BufferedImage(18 * scale, 18 * scale, BufferedImage.TYPE_INT_ARGB_PRE);
		    Graphics2D robotG = robot.createGraphics();
		    for (Component c : Simulator.getRobot()) {
		    	AffineTransform trans = AffineTransform.getTranslateInstance((c.getX() + 9f - c.getImage().getWidth() / scale / 2) * scale, (c.getY() + 9f - c.getImage().getHeight() / scale / 2) * scale);
		    	trans.rotate(Math.toRadians(-c.getRotation()), c.getImage().getWidth() / 2, c.getImage().getHeight() / 2);
		    	robotG.drawImage(c.getImage(), trans, null);
		    }
		    
		    robotG.drawLine(6 * scale, 18 * scale, 9 * scale, 0);
		    robotG.drawLine(13 * scale, 18 * scale, 9 * scale, 0);
		    
		    AffineTransform trans = AffineTransform.getTranslateInstance(Simulator.getRobotX() * scale, Simulator.getRobotY() * scale);
		    trans.rotate(Math.toRadians(-Simulator.getRobotRotation() + 90), robot.getWidth() / 2, robot.getHeight() / 2);
		    gg.drawImage(robot, trans, null);
		}
		
	}
	
}
