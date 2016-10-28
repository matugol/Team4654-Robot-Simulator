package com.qualcomm.robotcore.util;

public class Range {
	public static double scale(final double n, final double x1, final double x2, final double y1, final double y2) {
		final double a = (y1 - y2) / (x1 - x2);
		final double b = y1 - x1 * (y1 - y2) / (x1 - x2);
		return a * n + b;
	}

	public static double clip(final double number, final double min, final double max) {
		if (number < min) return min;
		if (number > max) return max;
		return number;
	}

	public static float clip(final float number, final float min, final float max) {
		if (number < min) return min;
		if (number > max) return max;
		return number;
	}

	public static int clip(final int number, final int min, final int max) {
		if (number < min) return min;
		if (number > max) return max;
		return number;
	}

	public static short clip(final short number, final short min, final short max) {
		if (number < min) return min;
		if (number > max) return max;
		return number;
	}

	public static byte clip(final byte number, final byte min, final byte max) {
		if (number < min) return min;
		if (number > max) return max;
		return number;
	}

	public static void throwIfRangeIsInvalid(final double number, final double min, final double max) throws IllegalArgumentException {
		if (number < min || number > max) {
			throw new IllegalArgumentException(String.format("number %f is invalid; valid ranges are %f..%f", new Object[] {Double.valueOf(number), Double.valueOf(min), Double.valueOf(max)}));
		}
	}

	public static void throwIfRangeIsInvalid(final int number, final int min, final int max) throws IllegalArgumentException {
		if (number < min || number > max) {
			throw new IllegalArgumentException(String.format("number %d is invalid; valid ranges are %d..%d", new Object[] {Integer.valueOf(number), Integer.valueOf(min), Integer.valueOf(max)}));
		}
	}
}
