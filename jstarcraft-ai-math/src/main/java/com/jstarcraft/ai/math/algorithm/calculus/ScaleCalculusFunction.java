package com.jstarcraft.ai.math.algorithm.calculus;

/**
 * 缩放微积分函数
 * 
 * @author Birdy
 *
 */
public class ScaleCalculusFunction implements CalculusFunction {

    private float n;

    public ScaleCalculusFunction(float n) {
        this.n = n;
    }

    @Override
    public float derivative(float x) {
        return n;
    }

    @Override
    public float primitive(float x) {
        return n * x;
    }

}
