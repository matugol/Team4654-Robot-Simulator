package com.qualcomm.simulator;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

import com.qualcomm.robotcore.hardware.Gamepad;

public class SimGamepad extends Gamepad implements KeyListener {

	private final BidirectionalHashMap<GamepadKey, Integer> keys = new BidirectionalHashMap<>();

	public SimGamepad() {
		this(null);
	}

	public SimGamepad(final GamepadCallback callback) {
		super(callback);

		// TODO

		keys.put(GamepadKey.A, KeyEvent.VK_Q);
		keys.put(GamepadKey.B, KeyEvent.VK_B);
		keys.put(GamepadKey.X, KeyEvent.VK_X);
		keys.put(GamepadKey.Y, KeyEvent.VK_Y);
		keys.put(GamepadKey.LEFT_BUMPER, KeyEvent.VK_R);
		keys.put(GamepadKey.RIGHT_BUMPER, KeyEvent.VK_Y);
		keys.put(GamepadKey.BACK, KeyEvent.VK_BACK_SPACE);
		keys.put(GamepadKey.START, KeyEvent.VK_ENTER);
		keys.put(GamepadKey.LEFT_STICK_BUTTON, KeyEvent.VK_C);
		keys.put(GamepadKey.RIGHT_STICK_BUTTON, KeyEvent.VK_SLASH);
		keys.put(GamepadKey.DPAD_LEFT, KeyEvent.VK_A);
		keys.put(GamepadKey.DPAD_RIGHT, KeyEvent.VK_D);
		keys.put(GamepadKey.DPAD_UP, KeyEvent.VK_W);
		keys.put(GamepadKey.DPAD_DOWN, KeyEvent.VK_S);
		keys.put(GamepadKey.LEFT_STICK_X_POS, KeyEvent.VK_H);
		keys.put(GamepadKey.LEFT_STICK_Y_POS, KeyEvent.VK_T);
		keys.put(GamepadKey.LEFT_STICK_X_NEG, KeyEvent.VK_F);
		keys.put(GamepadKey.LEFT_STICK_Y_NEG, KeyEvent.VK_G);
		keys.put(GamepadKey.RIGHT_STICK_X_POS, KeyEvent.VK_L);
		keys.put(GamepadKey.RIGHT_STICK_Y_POS, KeyEvent.VK_I);
		keys.put(GamepadKey.RIGHT_STICK_X_NEG, KeyEvent.VK_J);
		keys.put(GamepadKey.RIGHT_STICK_Y_NEG, KeyEvent.VK_K);
		keys.put(GamepadKey.LEFT_TRIGGER, KeyEvent.VK_5);
		keys.put(GamepadKey.RIGHT_TRIGGER, KeyEvent.VK_7);
	}

	@Override
	public void keyTyped(final KeyEvent e) {}

	@Override
	public void keyPressed(final KeyEvent e) {
		final GamepadKey key = keys.getKey(e.getKeyCode());
		if (key == null) return;
		update();
		switch (key) {
			case A:
				a = true;
				break;
			case B:
				b = true;
				break;
			case BACK:
				back = true;
				break;
			case DPAD_DOWN:
				dpad_down = true;
				break;
			case DPAD_LEFT:
				dpad_left = true;
				break;
			case DPAD_RIGHT:
				dpad_right = true;
				break;
			case DPAD_UP:
				dpad_up = true;
				break;
			case LEFT_BUMPER:
				left_bumper = true;
				break;
			case LEFT_STICK_BUTTON:
				left_stick_button = true;
				break;
			case LEFT_STICK_X_NEG:
				left_stick_x -= 1;
				break;
			case LEFT_STICK_X_POS:
				left_stick_x += 1;
				break;
			case LEFT_STICK_Y_NEG:
				left_stick_y -= 1;
				break;
			case LEFT_STICK_Y_POS:
				left_stick_y += 1;
				break;
			case LEFT_TRIGGER:
				left_trigger += 1;
				break;
			case RIGHT_BUMPER:
				right_bumper = true;
				break;
			case RIGHT_STICK_BUTTON:
				right_stick_button = true;
				break;
			case RIGHT_STICK_X_NEG:
				right_stick_x -= 1;
				break;
			case RIGHT_STICK_X_POS:
				right_stick_x += 1;
				break;
			case RIGHT_STICK_Y_NEG:
				right_stick_y -= 1;
				break;
			case RIGHT_STICK_Y_POS:
				right_stick_y += 1;
				break;
			case RIGHT_TRIGGER:
				right_trigger += 1;
				break;
			case START:
				start = true;
				break;
			case X:
				x = true;
				break;
			case Y:
				y = true;
				break;
			default:
				break;
		}
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		final GamepadKey key = keys.getKey(e.getKeyCode());
		if (key == null) return;
		update();
		switch (key) {
			case A:
				a = false;
				break;
			case B:
				b = false;
				break;
			case BACK:
				back = false;
				break;
			case DPAD_DOWN:
				dpad_down = false;
				break;
			case DPAD_LEFT:
				dpad_left = false;
				break;
			case DPAD_RIGHT:
				dpad_right = false;
				break;
			case DPAD_UP:
				dpad_up = false;
				break;
			case LEFT_BUMPER:
				left_bumper = false;
				break;
			case LEFT_STICK_BUTTON:
				left_stick_button = false;
				break;
			case LEFT_STICK_X_NEG:
				left_stick_x += 1;
				break;
			case LEFT_STICK_X_POS:
				left_stick_x -= 1;
				break;
			case LEFT_STICK_Y_NEG:
				left_stick_y += 1;
				break;
			case LEFT_STICK_Y_POS:
				left_stick_y -= 1;
				break;
			case LEFT_TRIGGER:
				left_trigger -= 1;
				break;
			case RIGHT_BUMPER:
				right_bumper = false;
				break;
			case RIGHT_STICK_BUTTON:
				right_stick_button = false;
				break;
			case RIGHT_STICK_X_NEG:
				right_stick_x += 1;
				break;
			case RIGHT_STICK_X_POS:
				right_stick_x -= 1;
				break;
			case RIGHT_STICK_Y_NEG:
				right_stick_y += 1;
				break;
			case RIGHT_STICK_Y_POS:
				right_stick_y -= 1;
				break;
			case RIGHT_TRIGGER:
				right_trigger -= 1;
				break;
			case START:
				start = false;
				break;
			case X:
				x = false;
				break;
			case Y:
				y = false;
				break;
			default:
				break;
		}
	}

	public static enum GamepadKey {
		A,
		B,
		X,
		Y,
		LEFT_BUMPER,
		RIGHT_BUMPER,
		BACK,
		START,
		LEFT_STICK_BUTTON,
		RIGHT_STICK_BUTTON,
		DPAD_LEFT,
		DPAD_RIGHT,
		DPAD_UP,
		DPAD_DOWN,
		LEFT_STICK_X_POS,
		LEFT_STICK_Y_POS,
		RIGHT_STICK_X_POS,
		RIGHT_STICK_Y_POS,
		LEFT_STICK_X_NEG,
		LEFT_STICK_Y_NEG,
		RIGHT_STICK_X_NEG,
		RIGHT_STICK_Y_NEG,
		LEFT_TRIGGER,
		RIGHT_TRIGGER
	}

	public void configure(final Component source, final InputMap inputMap, final ActionMap actionMap) {
		for (final GamepadKey key : keys.keySet()) {
			inputMap.put(KeyStroke.getKeyStroke(keys.get(key), 0, false), key.name() + "p");
			actionMap.put(key.name() + "p", new AbstractAction() {

				private static final long serialVersionUID = -5347494227971479307L;

				@Override
				public void actionPerformed(final ActionEvent e) {
					keyPressed(new KeyEvent(source, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keys.get(key), (char) 0));
				}
			});
			inputMap.put(KeyStroke.getKeyStroke(keys.get(key), 0, true), key.name() + "r");
			actionMap.put(key.name() + "r", new AbstractAction() {

				private static final long serialVersionUID = 8908276241707957463L;

				@Override
				public void actionPerformed(final ActionEvent e) {
					keyReleased(new KeyEvent(source, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, keys.get(key), (char) 0));
				}
			});
		}
	}

}
