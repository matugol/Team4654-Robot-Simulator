package com.qualcomm.robotcore.hardware;

public class Gamepad {

	public boolean a, b, x, y; 
	public boolean dpad_down, dpad_left, dpad_right, dpad_up;
	public boolean back, guide, start;
	public boolean left_bumper, left_stick_button, right_bumper, right_stick_button;
	public float left_stick_x, left_stick_y, left_trigger, right_stick_x, right_stick_y, right_trigger;
	
	public boolean atRest() {
		return left_stick_x == 0 && left_stick_y == 0 && left_trigger == 0 &&
		       right_stick_x == 0 && right_stick_y == 0 && right_trigger == 0;
	}
	
	public void copy(Gamepad gamepad) {
		a = gamepad.a;
		b = gamepad.b;
		x = gamepad.x;
		y = gamepad.y;
		
		dpad_down  = gamepad.dpad_down;
		dpad_left  = gamepad.dpad_left;
		dpad_right = gamepad.dpad_right;
		dpad_up    = gamepad.dpad_up;
		
		back  = gamepad.back;
		guide = gamepad.guide;
		start = gamepad.start;

		left_bumper        = gamepad.left_bumper;       
		left_stick_button  = gamepad.left_stick_button; 
		right_bumper       = gamepad.right_bumper;      
		right_stick_button = gamepad.right_stick_button;
		
		left_stick_x  = gamepad.left_stick_x;
		left_stick_y  = gamepad.left_stick_y;
		left_trigger  = gamepad.left_trigger;
		right_stick_x = gamepad.right_stick_x;
		right_stick_y = gamepad.right_stick_y;
		right_trigger = gamepad.right_trigger;
	}
	
	public Gamepad() {}
	
}
