package com.jstarcraft.ai.math.algorithm.calculus;

/**
 * 加法法则微积分函数
 * 
 * <pre>
 * 加法法则
 * </pre>
 * 
 * @author Birdy
 *
 */
public class AdditionCalculusFunction extends CombinationCalculusFunction {

    public AdditionCalculusFunction(CalculusFunction leftFunction, CalculusFunction rightFunction) {
        super(leftFunction, rightFunction);
    }

    @Override
    public float derivative(float x) {
        return leftFunction.derivative(x) + rightFunction.derivative(x);
    }

    @Override
    public float primitive(float x) {
        return leftFunction.primitive(x) + rightFunction.primitive(x);
    }

}
