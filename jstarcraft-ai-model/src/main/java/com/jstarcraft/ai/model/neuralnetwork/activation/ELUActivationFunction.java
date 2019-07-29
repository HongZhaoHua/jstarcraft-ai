package com.jstarcraft.ai.model.neuralnetwork.activation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * ELU激活函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * 
 * f(x) = alpha * (exp(x) - 1.0); x < 0 = x ; x>= 0
 * </pre>
 * 
 * @author Birdy
 *
 */
public class ELUActivationFunction implements ActivationFunction {

	public static final float DEFAULT_ALPHA = 1F;

	private float alpha;

	public ELUActivationFunction() {
		this(DEFAULT_ALPHA);
	}

	public ELUActivationFunction(float alpha) {
		this.alpha = alpha;
	}

	@Override
	public void forward(MathMatrix input, MathMatrix output) {
		output.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = input.getValue(row, column);
			if (value < 0F) {
				value = (float) ((FastMath.exp(value) - 1D) * alpha);
			}
			scalar.setValue(value);
		});
	}

	@Override
	public void forward(MathVector input, MathVector output) {
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			float value = input.getValue(index);
			if (value < 0F) {
				value = (float) ((FastMath.exp(value) - 1D) * alpha);
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
			value = value >= 0F ? 1F : (float) (FastMath.exp(value) * alpha);
			value *= error.getValue(row, column);
			scalar.setValue(value);
		});
	}

	@Override
	public void backward(MathVector input, MathVector error, MathVector output) {
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			float value = input.getValue(index);
			value = value >= 0F ? 1F : (float) (FastMath.exp(value) * alpha);
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
			ELUActivationFunction that = (ELUActivationFunction) object;
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
		return "ELUActivationFunction(alpha=" + alpha + ")";
	}

}
