package com.jstarcraft.ai.math.algorithm.calculus;

import java.util.Objects;

/**
 * 组合微积分函数
 * 
 * @author Birdy
 *
 */
public abstract class CombinationCalculusFunction implements CalculusFunction {

    protected CalculusFunction leftFunction;

    protected CalculusFunction rightFunction;

    protected CombinationCalculusFunction(CalculusFunction leftFunction, CalculusFunction rightFunction) {
        this.leftFunction = leftFunction;
        this.rightFunction = rightFunction;
    }

    public CalculusFunction getLeftFunction() {
        return leftFunction;
    }

    public CalculusFunction getRightFunction() {
        return rightFunction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftFunction, rightFunction);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        CombinationCalculusFunction that = (CombinationCalculusFunction) object;
        return Objects.equals(this.leftFunction, that.leftFunction) && Objects.equals(this.rightFunction, that.rightFunction);
    }

}
