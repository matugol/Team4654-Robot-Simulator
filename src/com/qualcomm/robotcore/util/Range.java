/*
 * Copyright (c) 2014, 2015 Qualcomm Technologies Inc
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 * 
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 * 
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.robotcore.util;

/**
 * Utility class for performing range operations
 */
public class Range {

	/*
	 * This class contains only static utility methods
	 */
	private Range() {}

	// ------------------------------------------------------------------------------------------------
	// Scaling
	// ------------------------------------------------------------------------------------------------

	/**
	 * Scale a number in the range of x1 to x2, to the range of y1 to y2
	 * 
	 * @param n number to scale
	 * @param x1 lower bound range of n
	 * @param x2 upper bound range of n
	 * @param y1 lower bound of scale
	 * @param y2 upper bound of scale
	 * @return a double scaled to a value between y1 and y2, inclusive
	 */
	public static double scale(final double n, final double x1, final double x2, final double y1, final double y2) {
		final double a = (y1 - y2) / (x1 - x2);
		final double b = y1 - x1 * (y1 - y2) / (x1 - x2);
		return a * n + b;
	}

	// ------------------------------------------------------------------------------------------------
	// Clipping
	// ------------------------------------------------------------------------------------------------

	/**
	 * clip 'number' if 'number' is less than 'min' or greater than 'max'
	 * 
	 * @param number number to test
	 * @param min minimum value allowed
	 * @param max maximum value allowed
	 */
	public static double clip(final double number, final double min, final double max) {
		if (number < min) return min;
		if (number > max) return max;
		return number;
	}

	/**
	 * clip 'number' if 'number' is less than 'min' or greater than 'max'
	 * 
	 * @param number number to test
	 * @param min minimum value allowed
	 * @param max maximum value allowed
	 */
	public static float clip(final float number, final float min, final float max) {
		if (number < min) return min;
		if (number > max) return max;
		return number;
	}

	/**
	 * clip 'number' if 'number' is less than 'min' or greater than 'max'
	 * 
	 * @param number number to test
	 * @param min minimum value allowed
	 * @param max maximum value allowed
	 */
	public static int clip(final int number, final int min, final int max) {
		if (number < min) return min;
		if (number > max) return max;
		return number;
	}

	/**
	 * clip 'number' if 'number' is less than 'min' or greater than 'max'
	 * 
	 * @param number number to test
	 * @param min minimum value allowed
	 * @param max maximum value allowed
	 */
	public static short clip(final short number, final short min, final short max) {
		if (number < min) return min;
		if (number > max) return max;
		return number;
	}

	/**
	 * clip 'number' if 'number' is less than 'min' or greater than 'max'
	 * 
	 * @param number number to test
	 * @param min minimum value allowed
	 * @param max maximum value allowed
	 */
	public static byte clip(final byte number, final byte min, final byte max) {
		if (number < min) return min;
		if (number > max) return max;
		return number;
	}

	// ------------------------------------------------------------------------------------------------
	// Validation
	// ------------------------------------------------------------------------------------------------

	/**
	 * Throw an IllegalArgumentException if 'number' is less than 'min' or greater than 'max'
	 * 
	 * @param number number to test
	 * @param min minimum value allowed
	 * @param max maximum value allowed
	 * @throws IllegalArgumentException if number is outside of range
	 */
	public static void throwIfRangeIsInvalid(final double number, final double min, final double max) throws IllegalArgumentException {
		if (number < min || number > max) {
			throw new IllegalArgumentException(String.format("number %f is invalid; valid ranges are %f..%f", number, min, max));
		}
	}

	/**
	 * Throw an IllegalArgumentException if 'number' is less than 'min' or greater than 'max'
	 * 
	 * @param number number to test
	 * @param min minimum value allowed
	 * @param max maximum value allowed
	 * @throws IllegalArgumentException if number is outside of range
	 */
	public static void throwIfRangeIsInvalid(final int number, final int min, final int max) throws IllegalArgumentException {
		if (number < min || number > max) {
			throw new IllegalArgumentException(String.format("number %d is invalid; valid ranges are %d..%d", number, min, max));
		}
	}
}
