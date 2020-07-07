package com.jstarcraft.ai.math.algorithm.calculus;

/**
 * 乘法法则微积分函数
 * 
 * <pre>
 * 乘法法则
 * </pre>
 * 
 * @author Birdy
 *
 */
public class MultiplicationCalculusFunction extends CombinationCalculusFunction {

    public MultiplicationCalculusFunction(CalculusFunction leftFunction, CalculusFunction rightFunction) {
        super(leftFunction, rightFunction);
    }

    @Override
    public float derivative(float x) {
        return leftFunction.primitive(x) * rightFunction.derivative(x) + rightFunction.primitive(x) * leftFunction.derivative(x);
    }

    @Override
    public float primitive(float x) {
        return leftFunction.primitive(x) * rightFunction.primitive(x);
    }

}
