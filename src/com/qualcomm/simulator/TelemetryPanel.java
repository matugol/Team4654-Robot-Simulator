package com.qualcomm.simulator;

import javax.swing.JTextArea;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TelemetryPanel extends JTextArea implements Telemetry {

	private static final long serialVersionUID = 5058933778597066132L;

	@Override
	public Item addData(final String caption, final Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Item addData(final String caption, final String format, final Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Line addLine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Line addLine(final String lineCaption) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public Log log() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeItem(final Item item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeLine(final Line line) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAutoClear(final boolean autoClear) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean update() {
		// TODO Auto-generated method stub
		return false;
	}

}
