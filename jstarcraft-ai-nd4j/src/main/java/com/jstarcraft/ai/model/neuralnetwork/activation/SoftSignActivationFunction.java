package com.jstarcraft.ai.model.neuralnetwork.activation;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * SoftSign激活函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * 
 * f_i(x) = x_i / (1+|x_i|)
 * </pre>
 * 
 * @author Birdy
 *
 */
public class SoftSignActivationFunction implements ActivationFunction {

	@Override
	public void forward(MathMatrix input, MathMatrix output) {
		output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = input.getValue(row, column);
			value = value / (1F + FastMath.abs(value));
			scalar.setValue(value);
		});
	}

	@Override
	public void forward(MathVector input, MathVector output) {
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			float value = input.getValue(index);
			value = value / (1F + FastMath.abs(value));
			scalar.setValue(value);
		});
	}

	@Override
	public void backward(MathMatrix input, MathMatrix error, MathMatrix output) {
		output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = input.getValue(row, column);
			value = 1F + FastMath.abs(value);
			value = 1F / (value * value);
			value *= error.getValue(row, column);
			scalar.setValue(value);
		});
	}

	@Override
	public void backward(MathVector input, MathVector error, MathVector output) {
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			float value = input.getValue(index);
			value = 1F + FastMath.abs(value);
			value = 1F / (value * value);
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
		return "SoftSignActivationFunction()";
	}

}
