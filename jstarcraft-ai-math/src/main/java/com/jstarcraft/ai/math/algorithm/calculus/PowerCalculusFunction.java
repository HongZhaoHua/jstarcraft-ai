package com.jstarcraft.ai.math.algorithm.calculus;

/**
 * 幂微积分函数
 * 
 * @author Birdy
 *
 */
public class PowerCalculusFunction implements CalculusFunction {

    private float n;

    public PowerCalculusFunction(float n) {
        this.n = n;
    }

    @Override
    public float derivative(float x) {
        return n * (float) Math.pow(x, n - 1);
    }

    @Override
    public float primitive(float x) {
        return (float) Math.pow(x, n);
    }

}
