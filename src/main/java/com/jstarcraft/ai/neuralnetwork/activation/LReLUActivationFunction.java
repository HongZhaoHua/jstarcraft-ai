package com.jstarcraft.ai.neuralnetwork.activation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Leaky ReLU激活函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * 
 * f(x) = max(0, x) + alpha * min(0, x)
 * </pre>
 * 
 * @author Birdy
 *
 */
public class LReLUActivationFunction implements ActivationFunction {

	public static final float DEFAULT_ALPHA = 0.01F;

	private float alpha;

	public LReLUActivationFunction() {
		this(DEFAULT_ALPHA);
	}

	public LReLUActivationFunction(float alpha) {
		this.alpha = alpha;
	}

	@Override
	public void forward(MathMatrix input, MathMatrix output) {
		output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = input.getValue(row, column);
			value = value < 0F ? alpha * value : value;
			scalar.setValue(value);
		});
	}

	@Override
	public void forward(MathVector input, MathVector output) {
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			float value = input.getValue(index);
			value = value < 0F ? alpha * value : value;
			scalar.setValue(value);
		});
	}

	@Override
	public void backward(MathMatrix input, MathMatrix error, MathMatrix output) {
		output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = input.getValue(row, column);
			value = (value >= 0F ? 1F : alpha);
			value *= error.getValue(row, column);
			scalar.setValue(value);
		});
	}

	@Override
	public void backward(MathVector input, MathVector error, MathVector output) {
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			float value = input.getValue(index);
			value = (value >= 0F ? 1F : alpha);
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
			LReLUActivationFunction that = (LReLUActivationFunction) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.alpha, that.alpha);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(alpha);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "LReLUActivationFunction(alpha=" + alpha + ")";
	}

}
