package com.qualcomm.simulator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class Window extends JFrame {

	private static final long serialVersionUID = -410931336559141265L;

	// public static final int scale = 5; // pixels per inch
	private static World world = new World();

	private static JList<RobotComponent> components = new JList<>();
	static {
		components.setCellRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = -2242260940827460562L;

			@Override
			public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
				final Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				if (!(value instanceof RobotComponent)) return c;
				final RobotComponent robotComponent = (RobotComponent) value;

				return robotComponent.getInfoBox();
			}

		});
	}

	public Window() {
		super("Team4654 Robot Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());

		world = new World();
// world.setPreferredSize(new Dimension(12 * 12 * scale, 12 * 12 * scale));
// components.setPreferredSize(new Dimension(250, 12 * 12 * scale));

		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, world, components);
		add(splitPane);

		setSize(640, 480);
		setExtendedState(MAXIMIZED_BOTH);

		// setIgnoreRepaint(true);
		// setSize(new Dimension(12 * 12 * scale, 12 * 12 * scale));
		setVisible(true);
	}

	public void refreshComponents() {
		components.setListData(Simulator.getRobot().toArray(new RobotComponent[Simulator.getRobot().size()]));
// components.setListData(new RobotComponent[] {null, null, null, null});
	}

	private static class World extends JPanel {

		private static final long serialVersionUID = -7276267169741993849L;

		@Override
		public void paint(final Graphics gg) {
			if (!(gg instanceof Graphics2D)) return;
			final Graphics2D g = (Graphics2D) gg;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			final float scale = Math.min(getWidth(), getHeight()) / 144F;
			final float dx = 0.5F * (getWidth() - 144 * scale);
			final float dy = 0.5F * (getHeight() - 144 * scale);

			for (int y = 0; y <= 144; y += 24) {
				g.drawLine((int) dx, (int) (dy + scale * y), (int) (dx + 144 * scale), (int) (dy + scale * y));
			}

			for (int x = 0; x <= 144; x += 24) {
				g.drawLine((int) (dx + scale * x), (int) dy, (int) (dx + scale * x), (int) (dy + 144 * scale));
			}

			final BufferedImage robot = new BufferedImage(18 * RobotComponent.IMAGE_SCALE, 18 * RobotComponent.IMAGE_SCALE, BufferedImage.TYPE_INT_ARGB_PRE);
			final Graphics2D robotG = robot.createGraphics();
			robotG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			for (final RobotComponent c : Simulator.getRobot()) {
				final AffineTransform trans = AffineTransform.getTranslateInstance((c.getX() + 9f) * RobotComponent.IMAGE_SCALE - c.getImage().getWidth() * 0.5F, (c.getY() + 9f) * RobotComponent.IMAGE_SCALE - c.getImage().getHeight() * 0.5F);
				trans.rotate(Math.toRadians(-c.getRotation()), c.getImage().getWidth() / 2f, c.getImage().getHeight() / 2f);
				robotG.drawImage(c.getImage(), trans, null);
			}

			robotG.drawLine(6 * RobotComponent.IMAGE_SCALE, 18 * RobotComponent.IMAGE_SCALE, 9 * RobotComponent.IMAGE_SCALE, 0);
			robotG.drawLine(13 * RobotComponent.IMAGE_SCALE, 18 * RobotComponent.IMAGE_SCALE, 9 * RobotComponent.IMAGE_SCALE, 0);

			final AffineTransform trans = AffineTransform.getTranslateInstance(dx + Simulator.getRobotX() * scale, dy + Simulator.getRobotY() * scale);
			trans.scale(scale / RobotComponent.IMAGE_SCALE, scale / RobotComponent.IMAGE_SCALE);
			trans.rotate(Math.toRadians(-Simulator.getRobotRotation() + 90), robot.getWidth() / 2f, robot.getHeight() / 2f);
			g.drawImage(robot, trans, null);
		}

	}

}
