package com.jstarcraft.ai.neuralnetwork.activation;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * HardSigmoid激活函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * 
 * f(x) = min(1, max(0, 0.2 * x + 0.5))
 * </pre>
 * 
 * @author Birdy
 *
 */
public class HardSigmoidActivationFunction implements ActivationFunction {

	@Override
	public void forward(MathMatrix input, MathMatrix output) {
		output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = input.getValue(row, column);
			value = Math.min(1F, Math.max(0F, 0.2F * value + 0.5F));
			scalar.setValue(value);
		});
	}

	@Override
	public void forward(MathVector input, MathVector output) {
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			float value = input.getValue(index);
			value = Math.min(1F, Math.max(0F, 0.2F * value + 0.5F));
			scalar.setValue(value);
		});
	}

	@Override
	public void backward(MathMatrix input, MathMatrix error, MathMatrix output) {
		output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = input.getValue(row, column);
			value = (value < -2.5F || value > 2.5F) ? 0F : 0.2F;
			value *= error.getValue(row, column);
			scalar.setValue(value);
		});
	}

	@Override
	public void backward(MathVector input, MathVector error, MathVector output) {
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			float value = input.getValue(index);
			value = (value < -2.5F || value > 2.5F) ? 0F : 0.2F;
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
		return "HardSigmoidActivationFunction()";
	}

}
