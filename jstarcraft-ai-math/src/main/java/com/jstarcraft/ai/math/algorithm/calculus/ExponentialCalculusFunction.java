package com.jstarcraft.ai.math.algorithm.calculus;

/**
 * 指数微积分函数
 * 
 * @author Birdy
 *
 */
public class ExponentialCalculusFunction implements CalculusFunction {

    private float n;

    public ExponentialCalculusFunction(float n) {
        this.n = n;
    }

    @Override
    public float derivative(float x) {
        return (float) (Math.pow(n, x) * Math.log(n));
    }

    @Override
    public float primitive(float x) {
        return (float) Math.pow(n, x);
    }

}
