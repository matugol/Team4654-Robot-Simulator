package com.qualcomm.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window extends JFrame {

	public static final int scale = 10; // pixels per inch
	
	public Window() {
		super("Team4654 Robot Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//setIgnoreRepaint(true);
		//setResizable(false);
		pack();
		setSize(new Dimension(800, 800));
		setVisible(true);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.RED);
	    //g.fillRect(0, 0, 99, 99);
	    //pack();
	    
	    BufferedImage robot = new BufferedImage(18 * scale, 18 * scale, BufferedImage.TYPE_INT_ARGB_PRE);
	    Graphics2D robotG = robot.createGraphics();
	    for (Component c : Simulator.getRobot()) {
	    	System.out.println(c + " (" + c.getX() + ", " + c.getY() + ")");
	    	System.out.println(c.getImage().getWidth());
	    	robotG.drawImage(c.getImage(), Math.round((c.getX() + 9f) * scale), Math.round((c.getY() + 9f) * scale), null);
	    }
	    
	    g.drawImage(robot, Math.round(Simulator.getRobotX() * scale), Math.round(Simulator.getRobotY() * scale), null);
	    
	}
	
}
