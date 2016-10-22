package com.qualcomm.robotcore.hardware;

/**
 * Created by bob on 2016-03-12.
 */
public class PWMOutputImplEx extends PWMOutputImpl implements PWMOutputEx {
	PWMOutputControllerEx controllerEx;

	public PWMOutputImplEx(final PWMOutputController controller, final int port) {
		super(controller, port);
		controllerEx = (PWMOutputControllerEx) controller;
	}

	@Override
	public void setPwmEnable() {
		controllerEx.setPwmEnable(port);
	}

	@Override
	public void setPwmDisable() {
		controllerEx.setPwmDisable(port);
	}

	@Override
	public boolean isPwmEnabled() {
		return controllerEx.isPwmEnabled(port);
	}
}
