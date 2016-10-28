package com.qualcomm.simulator;

import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;

import com.qualcomm.robotcore.hardware.Gamepad;

public class ControllerGamepad extends Gamepad {

	private final Controller controller;

	public ControllerGamepad(final Controller controller) {
		this(controller, null);
	}

	public ControllerGamepad(final Controller controller, final GamepadCallback callback) {
		super(callback);

		this.controller = controller;
	}

	@Override
	public void update() {
		controller.poll();
		a = controller.getComponents()[5].getPollData() == 1f;
		b = controller.getComponents()[6].getPollData() == 1f;
		x = controller.getComponents()[7].getPollData() == 1f;
		y = controller.getComponents()[8].getPollData() == 1f;
		left_bumper = controller.getComponents()[9].getPollData() == 1f;
		right_bumper = controller.getComponents()[10].getPollData() == 1f;
		back = controller.getComponents()[11].getPollData() == 1f;
		start = controller.getComponents()[12].getPollData() == 1f; // TODO better
		left_stick_button = controller.getComponents()[13].getPollData() == 1f;
		right_stick_button = controller.getComponents()[14].getPollData() == 1f;

		final float hat = controller.getComponents()[15].getPollData();
		dpad_left = hat == .875f || hat == 1f || hat == .125f;
		dpad_right = hat == .375f || hat == .5f || hat == .675f;
		dpad_up = hat == .125f || hat == .25f || hat == .375f;
		dpad_down = hat == .675f || hat == .75f || hat == .875f;

		left_stick_x = controller.getComponent(Identifier.Axis.X).getPollData();
		left_stick_y = controller.getComponent(Identifier.Axis.Y).getPollData() * -1;
		right_stick_x = controller.getComponent(Identifier.Axis.RX).getPollData();
		right_stick_y = controller.getComponent(Identifier.Axis.RY).getPollData() * -1;

		// TODO fix
		left_trigger = controller.getComponent(Identifier.Axis.Z).getPollData();
		// opMode.gamepad1.right_trigger = controller1.getComponent(Identifier.Axis.RZ).getPollData();

		super.update();
	}

}
