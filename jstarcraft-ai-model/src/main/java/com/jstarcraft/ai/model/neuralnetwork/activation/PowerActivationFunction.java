package com.jstarcraft.ai.model.neuralnetwork.activation;

import org.apache.commons.math3.util.FastMath;

import com.google.common.base.Objects;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Power激活函数
 * 
 * <pre>
 * f(x) = x ^ n
 * </pre>
 * 
 * @author Birdy
 *
 */
public class PowerActivationFunction implements ActivationFunction {

    private int n;

    PowerActivationFunction() {
    }

    public PowerActivationFunction(int n) {
        this.n = n;
    }

    @Override
    public void forward(MathMatrix input, MathMatrix output) {
        output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
            int row = scalar.getRow();
            int column = scalar.getColumn();
            scalar.setValue((float) FastMath.pow(input.getValue(row, column), n));
        });
    }

    @Override
    public void forward(MathVector input, MathVector output) {
        output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            int index = scalar.getIndex();
            scalar.setValue((float) FastMath.pow(input.getValue(index), n));
        });
    }

    @Override
    public void backward(MathMatrix input, MathMatrix error, MathMatrix output) {
        output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
            int row = scalar.getRow();
            int column = scalar.getColumn();
            scalar.setValue((float) (n * FastMath.pow(input.getValue(row, column), n - 1)) * error.getValue(row, column));
        });
    }

    @Override
    public void backward(MathVector input, MathVector error, MathVector output) {
        output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            int index = scalar.getIndex();
            scalar.setValue((float) (n * FastMath.pow(input.getValue(index), n - 1)) * error.getValue(index));
        });
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        PowerActivationFunction that = (PowerActivationFunction) object;
        return this.n == that.n;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(n);
    }

    @Override
    public String toString() {
        return "PowerActivationFunction(n=" + n + ")";
    }

}
