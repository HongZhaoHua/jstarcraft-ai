package com.jstarcraft.ai.math.algorithm.calculus;

/**
 * 链式法则微积分函数
 * 
 * <pre>
 * 链式法则
 * </pre>
 * 
 * @author Birdy
 *
 */
public class ChainCalculusFunction extends CombinationCalculusFunction {

    public ChainCalculusFunction(CalculusFunction leftFunction, CalculusFunction rightFunction) {
        super(leftFunction, rightFunction);
    }

    @Override
    public float derivative(float x) {
        return leftFunction.derivative(rightFunction.primitive(x)) * rightFunction.derivative(x);
    }

    @Override
    public float primitive(float x) {
        return leftFunction.primitive(rightFunction.primitive(x));
    }

}
