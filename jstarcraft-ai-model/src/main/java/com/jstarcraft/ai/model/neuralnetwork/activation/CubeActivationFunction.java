package com.jstarcraft.ai.model.neuralnetwork.activation;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Cube激活函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * 
 * f(x) = x ^ 3
 * </pre>
 * 
 * @author Birdy
 *
 */
public class CubeActivationFunction implements ActivationFunction {

	@Override
	public void forward(MathMatrix input, MathMatrix output) {
		output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			scalar.setValue((float) FastMath.pow(input.getValue(row, column), 3));
		});
	}

	@Override
	public void forward(MathVector input, MathVector output) {
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			scalar.setValue((float) FastMath.pow(input.getValue(index), 3));
		});
	}

	@Override
	public void backward(MathMatrix input, MathMatrix error, MathMatrix output) {
		output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			scalar.setValue((float) (3 * FastMath.pow(input.getValue(row, column), 2)) * error.getValue(row, column));
		});
	}

	@Override
	public void backward(MathVector input, MathVector error, MathVector output) {
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			scalar.setValue((float) (3 * FastMath.pow(input.getValue(index), 2)) * error.getValue(index));
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
		return "CubeActivationFunction()";
	}

}
