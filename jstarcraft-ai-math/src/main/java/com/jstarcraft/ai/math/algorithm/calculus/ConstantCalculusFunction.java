package com.jstarcraft.ai.math.algorithm.calculus;

/**
 * 常量微积分函数
 * 
 * @author Birdy
 *
 */
public class ConstantCalculusFunction implements CalculusFunction {

    private float n;

    public ConstantCalculusFunction(float n) {
        this.n = n;
    }

    @Override
    public float derivative(float x) {
        return 0;
    }

    @Override
    public float primitive(float x) {
        return n;
    }

}
