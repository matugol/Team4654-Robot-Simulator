package com.qualcomm.simulator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {

	private static final long serialVersionUID = -1022517881822368634L;

	public static final int SCALE = 5; // pixels per inch
	private static World world;

	public Window() {
		super("Team4654 Robot Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		world = new World();
		add(world);

		// setIgnoreRepaint(true);
		// setResizable(false);
		// pack();
		setSize(new Dimension(800, 800));
		setVisible(true);
	}

	private class World extends JPanel {

		private static final long serialVersionUID = -7276267169741993849L;

		@Override
		public void paint(final Graphics g) {
			final Graphics2D gg = (Graphics2D) g;
			final BufferedImage robot = new BufferedImage(18 * SCALE, 18 * SCALE, BufferedImage.TYPE_INT_ARGB_PRE);
			final Graphics2D robotG = robot.createGraphics();
			for (final Component c : Simulator.getRobot()) {
				final AffineTransform trans = AffineTransform.getTranslateInstance((c.getX() + 9f - c.getImage().getWidth() / SCALE / 2) * SCALE, (c.getY() + 9f - c.getImage().getHeight() / SCALE / 2) * SCALE);
				trans.rotate(Math.toRadians(-c.getRotation()), c.getImage().getWidth() / 2, c.getImage().getHeight() / 2);
				robotG.drawImage(c.getImage(), trans, null);
			}

			robotG.drawLine(6 * SCALE, 18 * SCALE, 9 * SCALE, 0);
			robotG.drawLine(13 * SCALE, 18 * SCALE, 9 * SCALE, 0);

			final AffineTransform trans = AffineTransform.getTranslateInstance(Simulator.getRobotX() * SCALE, Simulator.getRobotY() * SCALE);
			trans.rotate(Math.toRadians(-Simulator.getRobotRotation() + 90), robot.getWidth() / 2, robot.getHeight() / 2);
			gg.drawImage(robot, trans, null);
		}

	}

}
