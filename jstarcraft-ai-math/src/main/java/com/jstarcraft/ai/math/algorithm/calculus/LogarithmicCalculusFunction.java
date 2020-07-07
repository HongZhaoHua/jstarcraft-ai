package com.jstarcraft.ai.math.algorithm.calculus;

import com.jstarcraft.ai.math.MathUtility;

/**
 * 对数微积分函数
 * 
 * @author Birdy
 *
 */
public class LogarithmicCalculusFunction implements CalculusFunction {

    private float n;

    public LogarithmicCalculusFunction(float n) {
        this.n = n;
    }

    @Override
    public float derivative(float x) {
        return 1F / (float) (x * Math.log(n));
    }

    @Override
    public float primitive(float x) {
        return MathUtility.logarithm(x, n);
    }

}
