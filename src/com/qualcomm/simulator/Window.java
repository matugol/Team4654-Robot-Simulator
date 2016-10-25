package com.qualcomm.simulator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

public class Window extends JFrame {

	private static final long serialVersionUID = -410931336559141265L;

	public static final int scale = 5; // pixels per inch
	private static World world;
	
	private static JList<RobotComponent> components = new JList<>();
	static {
		components.setCellRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = -2242260940827460562L;
			
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				
				if (!(value instanceof RobotComponent)) return c;
				RobotComponent robotComponent = (RobotComponent) value;
				
				return c;
			}

			
		});
	}
	
	public Window() {
		super("Team4654 Robot Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new FlowLayout());
		
		world = new World();
		world.setPreferredSize(new Dimension(12 * 12 * scale, 12 * 12 * scale));
		add(world);
		components.setPreferredSize(new Dimension(250, 12 * 12 * scale));
		add(components);
		
		//setIgnoreRepaint(true);
		setResizable(false);
		pack();
		//setSize(new Dimension(12 * 12 * scale, 12 * 12 * scale));
		setVisible(true);
	}
	
	public void refreshComponents() {
		RobotComponent[] temp = new RobotComponent[Simulator.getRobot().size()];
		Simulator.getRobot().toArray(temp);
		components.setListData(temp);
	}
	
	private class World extends JPanel {
	
		private static final long serialVersionUID = -7276267169741993849L;

		public void paint(Graphics g) {	  
			Graphics2D gg = (Graphics2D) g;
		    BufferedImage robot = new BufferedImage(18 * scale, 18 * scale, BufferedImage.TYPE_INT_ARGB_PRE);
		    Graphics2D robotG = robot.createGraphics();
		    for (RobotComponent c : Simulator.getRobot()) {
		    	AffineTransform trans = AffineTransform.getTranslateInstance((c.getX() + 9f - c.getImage().getWidth() / scale / 2f) * scale, (c.getY() + 9f - c.getImage().getHeight() / scale / 2f) * scale);
		    	trans.rotate(Math.toRadians(-c.getRotation()), c.getImage().getWidth() / 2f, c.getImage().getHeight() / 2f);
		    	robotG.drawImage(c.getImage(), trans, null);
		    }
		    
		    robotG.drawLine(6 * scale, 18 * scale, 9 * scale, 0);
		    robotG.drawLine(13 * scale, 18 * scale, 9 * scale, 0);
		    
		    AffineTransform trans = AffineTransform.getTranslateInstance(Simulator.getRobotX() * scale, Simulator.getRobotY() * scale);
		    trans.rotate(Math.toRadians(-Simulator.getRobotRotation() + 90), robot.getWidth() / 2f, robot.getHeight() / 2f);
		    gg.drawImage(robot, trans, null);
		}
		
	}
	
}
