package com.qualcomm.robotcore.hardware;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import com.qualcomm.robotcore.util.Range;

public class Gamepad {
	public float left_stick_x = 0.0F;
	public float left_stick_y = 0.0F;
	public float right_stick_x = 0.0F;
	public float right_stick_y = 0.0F;
	public boolean dpad_up = false;
	public boolean dpad_down = false;
	public boolean dpad_left = false;
	public boolean dpad_right = false;
	public boolean a = false;
	public boolean b = false;
	public boolean x = false;
	public boolean y = false;
	public boolean guide = false;
	public boolean start = false;
	public boolean back = false;
	public boolean left_bumper = false;
	public boolean right_bumper = false;
	public boolean left_stick_button = false;
	public boolean right_stick_button = false;
	public float left_trigger = 0.0F;
	public float right_trigger = 0.0F;
	public byte user = -1;
	public int id = -1;
	public long timestamp = 0L;
	protected float dpadThreshold = 0.2F;
	protected float joystickDeadzone = 0.2F;

	private final GamepadCallback callback;

	public Gamepad() {
		this(null);
	}

	public Gamepad(final GamepadCallback callback) {
		this.callback = callback;
	}

	public void copy(final Gamepad gamepad) {
		fromByteArray(gamepad.toByteArray());
	}

	public void reset() {
		copy(new Gamepad());
	}

	public void setJoystickDeadzone(final float deadzone) {
		if (deadzone < 0.0F || deadzone > 1.0F) {
			throw new IllegalArgumentException("deadzone cannot be greater than max joystick value");
		}

		joystickDeadzone = deadzone;
	}

	public void update() {
		timestamp = System.currentTimeMillis();
		callCallback();
	}

// public void update(final MotionEvent event) {
// id = event.getDeviceId();
// timestamp = event.getEventTime();
//
// left_stick_x = cleanMotionValues(event.getAxisValue(0));
// left_stick_y = cleanMotionValues(event.getAxisValue(1));
// right_stick_x = cleanMotionValues(event.getAxisValue(11));
// right_stick_y = cleanMotionValues(event.getAxisValue(14));
// left_trigger = event.getAxisValue(17);
// right_trigger = event.getAxisValue(18);
// dpad_down = event.getAxisValue(16) > dpadThreshold;
// dpad_up = event.getAxisValue(16) < -dpadThreshold;
// dpad_right = event.getAxisValue(15) > dpadThreshold;
// dpad_left = event.getAxisValue(15) < -dpadThreshold;
//
// callCallback();
// }
//
// public void update(final KeyEvent event) {
// id = event.getDeviceId();
// timestamp = event.getEventTime();
//
// final int key = event.getKeyCode();
// if (key == 19) {
// dpad_up = pressed(event);
// } else if (key == 20) {
// dpad_down = pressed(event);
// } else if (key == 22) {
// dpad_right = pressed(event);
// } else if (key == 21) {
// dpad_left = pressed(event);
// } else if (key == 96) {
// a = pressed(event);
// } else if (key == 97) {
// b = pressed(event);
// } else if (key == 99) {
// x = pressed(event);
// } else if (key == 100) {
// y = pressed(event);
// } else if (key == 110) {
// guide = pressed(event);
// } else if (key == 108) {
// start = pressed(event);
// } else if (key == 109) {
// back = pressed(event);
// } else if (key == 103) {
// right_bumper = pressed(event);
// } else if (key == 102) {
// left_bumper = pressed(event);
// } else if (key == 106) {
// left_stick_button = pressed(event);
// } else if (key == 107) {
// right_stick_button = pressed(event);
// }
// callCallback();
// }

	public byte[] toByteArray() {
		final ByteBuffer buffer = getWriteBuffer(42);
		try {
			int buttons = 0;

			buffer.put((byte) 2);
			buffer.putInt(id);
			buffer.putLong(timestamp).array();
			buffer.putFloat(left_stick_x).array();
			buffer.putFloat(left_stick_y).array();
			buffer.putFloat(right_stick_x).array();
			buffer.putFloat(right_stick_y).array();
			buffer.putFloat(left_trigger).array();
			buffer.putFloat(right_trigger).array();

			buttons = (buttons << 1) + (left_stick_button ? 1 : 0);
			buttons = (buttons << 1) + (right_stick_button ? 1 : 0);
			buttons = (buttons << 1) + (dpad_up ? 1 : 0);
			buttons = (buttons << 1) + (dpad_down ? 1 : 0);
			buttons = (buttons << 1) + (dpad_left ? 1 : 0);
			buttons = (buttons << 1) + (dpad_right ? 1 : 0);
			buttons = (buttons << 1) + (a ? 1 : 0);
			buttons = (buttons << 1) + (b ? 1 : 0);
			buttons = (buttons << 1) + (x ? 1 : 0);
			buttons = (buttons << 1) + (y ? 1 : 0);
			buttons = (buttons << 1) + (guide ? 1 : 0);
			buttons = (buttons << 1) + (start ? 1 : 0);
			buttons = (buttons << 1) + (back ? 1 : 0);
			buttons = (buttons << 1) + (left_bumper ? 1 : 0);
			buttons = (buttons << 1) + (right_bumper ? 1 : 0);
			buffer.putInt(buttons);

			buffer.put(user);
		} catch (final BufferOverflowException e) {}

		return buffer.array();
	}

	public void fromByteArray(final byte[] byteArray) {
		final ByteBuffer byteBuffer = getReadBuffer(byteArray);

		int buttons = 0;

		final byte version = byteBuffer.get();

		if (version >= 1) {
			id = byteBuffer.getInt();
			timestamp = byteBuffer.getLong();
			left_stick_x = byteBuffer.getFloat();
			left_stick_y = byteBuffer.getFloat();
			right_stick_x = byteBuffer.getFloat();
			right_stick_y = byteBuffer.getFloat();
			left_trigger = byteBuffer.getFloat();
			right_trigger = byteBuffer.getFloat();

			buttons = byteBuffer.getInt();
			left_stick_button = (buttons & 0x4000) != 0;
			right_stick_button = (buttons & 0x2000) != 0;
			dpad_up = (buttons & 0x1000) != 0;
			dpad_down = (buttons & 0x800) != 0;
			dpad_left = (buttons & 0x400) != 0;
			dpad_right = (buttons & 0x200) != 0;
			a = (buttons & 0x100) != 0;
			b = (buttons & 0x80) != 0;
			x = (buttons & 0x40) != 0;
			y = (buttons & 0x20) != 0;
			guide = (buttons & 0x10) != 0;
			start = (buttons & 0x8) != 0;
			back = (buttons & 0x4) != 0;
			left_bumper = (buttons & 0x2) != 0;
			right_bumper = (buttons & 0x1) != 0;
		}

		if (version >= 2) {
			user = byteBuffer.get();
		}

		callCallback();
	}

	public boolean atRest() {
		return left_stick_x == 0.0F && left_stick_y == 0.0F && right_stick_x == 0.0F && right_stick_y == 0.0F && left_trigger == 0.0F && right_trigger == 0.0F;
	}

	public String type() {
		return "Standard";
	}

	@Override
	public String toString() {
		String buttons = new String();
		if (dpad_up) buttons = buttons + "dpad_up ";
		if (dpad_down) buttons = buttons + "dpad_down ";
		if (dpad_left) buttons = buttons + "dpad_left ";
		if (dpad_right) buttons = buttons + "dpad_right ";
		if (a) buttons = buttons + "a ";
		if (b) buttons = buttons + "b ";
		if (x) buttons = buttons + "x ";
		if (y) buttons = buttons + "y ";
		if (guide) buttons = buttons + "guide ";
		if (start) buttons = buttons + "start ";
		if (back) buttons = buttons + "back ";
		if (left_bumper) buttons = buttons + "left_bumper ";
		if (right_bumper) buttons = buttons + "right_bumper ";
		if (left_stick_button) buttons = buttons + "left stick button ";
		if (right_stick_button) {
			buttons = buttons + "right stick button ";
		}
		return String.format("ID: %2d user: %2d lx: % 1.2f ly: % 1.2f rx: % 1.2f ry: % 1.2f lt: %1.2f rt: %1.2f %s", new Object[] {Integer.valueOf(id), Byte.valueOf(user), Float.valueOf(left_stick_x), Float.valueOf(left_stick_y), Float.valueOf(right_stick_x), Float.valueOf(right_stick_y), Float.valueOf(left_trigger), Float.valueOf(right_trigger), buttons});
	}

	protected float cleanMotionValues(final float number) {
		if (number < joystickDeadzone && number > -joystickDeadzone) {
			return 0.0F;
		}

		if (number > 1.0F) return 1.0F;
		if (number < -1.0F) {
			return -1.0F;
		}

		if (number > 0.0F) {
			return (float) Range.scale(number, joystickDeadzone, 1.0D, 0.0D, 1.0D);
		} else {
			return (float) Range.scale(number, -joystickDeadzone, -1.0D, 0.0D, -1.0D);
		}
	}

	protected void callCallback() {
		if (callback != null) {
			callback.gamepadChanged(this);
		}
	}

	public static interface GamepadCallback {
		public void gamepadChanged(Gamepad gamepad);
	}

	protected int sequenceNumber;

	protected ByteBuffer allocateWholeWriteBuffer(final int overallSize) {
		return ByteBuffer.allocate(overallSize);
	}

	protected ByteBuffer getWriteBuffer(final int payloadSize) {
		final ByteBuffer result = allocateWholeWriteBuffer(5 + payloadSize);

		result.put((byte) 0);
		result.putShort((short) payloadSize);
		result.putShort((short) sequenceNumber);

		return result;
	}

	public void setSequenceNumber(final short sequenceNumber) {
		this.sequenceNumber = sequenceNumber & 0xFFFF;
	}

	protected ByteBuffer getReadBuffer(final byte[] byteArray) {
		final int cbHeaderWithoutSeqNum = 3;
		final ByteBuffer result = ByteBuffer.wrap(byteArray, cbHeaderWithoutSeqNum, byteArray.length - cbHeaderWithoutSeqNum);

		setSequenceNumber(result.getShort());

		return result;
	}
}
