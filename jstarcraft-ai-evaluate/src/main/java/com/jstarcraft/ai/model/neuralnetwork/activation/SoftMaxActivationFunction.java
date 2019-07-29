package com.jstarcraft.ai.model.neuralnetwork.activation;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.DefaultScalar;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;

/**
 * SoftMax激活函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * 
 * f_i(x) = exp(x_i - shift) / sum_j exp(x_j - shift) where shift = max_i(x_i)
 * </pre>
 * 
 * @author Birdy
 *
 */
public class SoftMaxActivationFunction implements ActivationFunction {

	@Override
	public void forward(MathMatrix input, MathMatrix output) {
		for (int row = 0; row < output.getRowSize(); row++) {
			double maximum = Double.NEGATIVE_INFINITY;
			MathVector inputVector = input.getRowVector(row);
			for (VectorScalar term : inputVector) {
				maximum = FastMath.max(maximum, term.getValue());
			}
			double shift = maximum;
			DefaultScalar sum = DefaultScalar.getInstance();
			sum.setValue(0F);
			MathVector outputVector = output.getRowVector(row);
			outputVector.iterateElement(MathCalculator.SERIAL, (scalar) -> {
				int index = scalar.getIndex();
				float value = inputVector.getValue(index);
				value = (float) FastMath.exp(value - shift);
				sum.shiftValue(value);
				scalar.setValue(value);
			});
			outputVector.iterateElement(MathCalculator.SERIAL, (scalar) -> {
				int index = scalar.getIndex();
				float value = scalar.getValue();
				scalar.setValue(value / sum.getValue());
			});
		}
	}

	@Override
	public void forward(MathVector input, MathVector output) {
		double maximum = Double.NEGATIVE_INFINITY;
		for (VectorScalar term : input) {
			maximum = FastMath.max(maximum, term.getValue());
		}
		double shift = maximum;
		DefaultScalar sum = DefaultScalar.getInstance();
		sum.setValue(0F);
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			float value = input.getValue(index);
			value = (float) FastMath.exp(value - shift);
			sum.shiftValue(value);
			scalar.setValue(value);
		});
		output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			int index = scalar.getIndex();
			float value = scalar.getValue();
			scalar.setValue(value / sum.getValue());
		});
	}

	@Override
	public void backward(MathMatrix input, MathMatrix error, MathMatrix output) {
		{
			for (int row = 0; row < output.getRowSize(); row++) {
				double maximum = Double.NEGATIVE_INFINITY;
				MathVector vector = input.getRowVector(row);
				for (VectorScalar term : vector) {
					maximum = FastMath.max(maximum, term.getValue());
				}
				double shift = maximum;
				DefaultScalar sum = DefaultScalar.getInstance();
				sum.setValue(0F);
				MathVector outputVector = output.getRowVector(row);
				outputVector.iterateElement(MathCalculator.SERIAL, (scalar) -> {
					int index = scalar.getIndex();
					float value = vector.getValue(index);
					value = (float) FastMath.exp(value - shift);
					sum.shiftValue(value);
					scalar.setValue(value);
				});
				outputVector.iterateElement(MathCalculator.SERIAL, (scalar) -> {
					int index = scalar.getIndex();
					float value = scalar.getValue();
					scalar.setValue(value / sum.getValue());
				});
			}
		}

		{
			for (int row = 0; row < output.getRowSize(); row++) {
				MathVector vector = output.getRowVector(row);
				float sum = 0F;
				for (VectorScalar term : vector) {
					sum += term.getValue() * error.getValue(row, term.getIndex());
				}
				float shift = sum;
				for (VectorScalar term : vector) {
					float value = term.getValue();
					value *= (error.getValue(row, term.getIndex()) - shift);
					term.setValue(value);
				}
			}
		}
	}

	@Override
	public void backward(MathVector input, MathVector error, MathVector output) {
		{
			double maximum = Double.NEGATIVE_INFINITY;
			for (VectorScalar term : input) {
				maximum = FastMath.max(maximum, term.getValue());
			}
			double shift = maximum;
			DefaultScalar sum = DefaultScalar.getInstance();
			sum.setValue(0F);
			output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
				int index = scalar.getIndex();
				float value = input.getValue(index);
				value = (float) FastMath.exp(value - shift);
				sum.shiftValue(value);
				scalar.setValue(value);
			});
			output.iterateElement(MathCalculator.SERIAL, (scalar) -> {
				int index = scalar.getIndex();
				float value = scalar.getValue();
				scalar.setValue(value / sum.getValue());
			});
		}

		{
			float sum = 0F;
			for (VectorScalar term : output) {
				sum += term.getValue() * error.getValue(term.getIndex());
			}
			float shift = sum;
			for (VectorScalar term : output) {
				float value = term.getValue();
				value *= (error.getValue(term.getIndex()) - shift);
				term.setValue(value);
			}
		}
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
		return "SoftmaxActivationFunction()";
	}

}
