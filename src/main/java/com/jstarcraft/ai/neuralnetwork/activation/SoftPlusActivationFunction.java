package com.jstarcraft.ai.neuralnetwork.activation;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.utility.MathUtility;

/**
 * SoftPlus激活函数
 * 
 * @author Birdy
 *
 */
public class SoftPlusActivationFunction implements ActivationFunction {

	private boolean threshold;

	public SoftPlusActivationFunction() {
		this(false);
	}

	public SoftPlusActivationFunction(boolean threshold) {
		this.threshold = threshold;
	}

	@Override
	public void forward(MathMatrix input, MathMatrix output) {
		output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = input.getValue(row, column);
			value = (float) FastMath.log(1F + FastMath.exp(value));
			if (threshold && (Float.isNaN(value) || Float.isInfinite(value))) {
				value = MathUtility.EPSILON;
			}
			scalar.setValue(value);
		});
	}

	@Override
	public void forward(MathVector input, MathVector output) {
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			float value = input.getValue(index);
			value = (float) FastMath.log(1F + FastMath.exp(value));
			if (threshold && (Float.isNaN(value) || Float.isInfinite(value))) {
				value = MathUtility.EPSILON;
			}
			scalar.setValue(value);
		});
	}

	@Override
	public void backward(MathMatrix input, MathMatrix error, MathMatrix output) {
		output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = input.getValue(row, column);
			value = (float) (1F / (1F + FastMath.exp(-value)));
			if (threshold && (Float.isNaN(value) || Float.isInfinite(value))) {
				value = MathUtility.EPSILON;
			}
			value *= error.getValue(row, column);
			scalar.setValue(value);
		});
	}

	@Override
	public void backward(MathVector input, MathVector error, MathVector output) {
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			float value = input.getValue(index);
			value = (float) (1F / (1F + FastMath.exp(-value)));
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
		return "SoftPlusActivationFunction(threshold=" + threshold + ")";
	}

}
