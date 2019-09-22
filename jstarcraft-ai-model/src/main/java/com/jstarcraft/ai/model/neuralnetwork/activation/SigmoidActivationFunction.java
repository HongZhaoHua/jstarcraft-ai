package com.jstarcraft.ai.model.neuralnetwork.activation;

import org.apache.commons.math3.util.FastMath;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.transforms.Sigmoid;
import org.nd4j.linalg.api.ops.impl.transforms.SigmoidDerivative;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.math.MathUtility;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.Nd4jMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Sigmoid激活函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * 
 * f(x) = 1 / (1 + exp(-x))
 * </pre>
 * 
 * @author Birdy
 *
 */
public class SigmoidActivationFunction implements ActivationFunction {

    private boolean threshold;

    public SigmoidActivationFunction() {
        this(false);
    }

    public SigmoidActivationFunction(boolean threshold) {
        this.threshold = threshold;
    }

    @Override
    public void forward(MathMatrix input, MathMatrix output) {
        if (input instanceof Nd4jMatrix && output instanceof Nd4jMatrix) {
            INDArray inputArray = Nd4jMatrix.class.cast(input).getArray();
            INDArray outputArray = Nd4jMatrix.class.cast(output).getArray();
            Nd4j.getExecutioner().execAndReturn(new Sigmoid(inputArray, outputArray));
        } else {
            output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
                int row = scalar.getRow();
                int column = scalar.getColumn();
                float value = input.getValue(row, column);
                value = (float) (1F / (1F + FastMath.exp(-value)));
                if (threshold && (Float.isNaN(value) || Float.isInfinite(value))) {
                    value = MathUtility.EPSILON;
                }
                scalar.setValue(value);
            });
        }
    }

    @Override
    public void forward(MathVector input, MathVector output) {
        output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            int index = scalar.getIndex();
            float value = input.getValue(index);
            value = (float) (1F / (1F + FastMath.exp(-value)));
            if (threshold && (Float.isNaN(value) || Float.isInfinite(value))) {
                value = MathUtility.EPSILON;
            }
            scalar.setValue(value);
        });
    }

    @Override
    public void backward(MathMatrix input, MathMatrix error, MathMatrix output) {
        if (input instanceof Nd4jMatrix && output instanceof Nd4jMatrix && error instanceof Nd4jMatrix) {
            INDArray inputArray = Nd4jMatrix.class.cast(input).getArray();
            INDArray outputArray = Nd4jMatrix.class.cast(output).getArray();
            INDArray errorArray = Nd4jMatrix.class.cast(error).getArray();
            Nd4j.getExecutioner().execAndReturn(new SigmoidDerivative(inputArray, outputArray));
            outputArray.muli(errorArray);
        } else {
            output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
                int row = scalar.getRow();
                int column = scalar.getColumn();
                float value = input.getValue(row, column);
                value = (float) (1F / (1F + FastMath.exp(-value)));
                value = value * (1F - value);
                if (threshold && (Float.isNaN(value) || Float.isInfinite(value))) {
                    value = MathUtility.EPSILON;
                }
                value *= error.getValue(row, column);
                scalar.setValue(value);
            });
        }
    }

    @Override
    public void backward(MathVector input, MathVector error, MathVector output) {
        output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            int index = scalar.getIndex();
            float value = input.getValue(index);
            value = (float) (1F / (1F + FastMath.exp(-value)));
            value = value * (1F - value);
            if (threshold && (Float.isNaN(value) || Float.isInfinite(value))) {
                value = MathUtility.EPSILON;
            }
            value *= error.getValue(index);
            scalar.setValue(value);
        });
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "SigmoidActivationFunction(threshold=" + threshold + ")";
    }

}
