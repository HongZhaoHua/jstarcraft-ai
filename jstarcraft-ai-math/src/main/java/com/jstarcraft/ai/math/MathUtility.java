package com.jstarcraft.ai.math;

public class MathUtility {

	public final static float EPSILON = 1E-5F;

	private MathUtility() {
	}

	public static boolean equal(float left, float right) {
		return Math.abs(left - right) < EPSILON;
	}

	/**
	 * 计算对数
	 * 
	 * @param number
	 * @param base
	 * @return
	 */
	public static float logarithm(float number, int base) {
		return (float) (Math.log(number) / Math.log(base));
	}

}
