package com.jstarcraft.ai.math.algorithm.calculus;

/**
 * 偏移微积分函数
 * 
 * @author Birdy
 *
 */
public class ShiftCalculusFunction implements CalculusFunction {

    private float n;

    public ShiftCalculusFunction(float n) {
        this.n = n;
    }

    @Override
    public float derivative(float x) {
        return 1;
    }

    @Override
    public float primitive(float x) {
        return n + x;
    }

}
