package com.jstarcraft.ai.neuralnetwork.activation;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * 恒等激活函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * 
 * f(x) = x
 * </pre>
 * 
 * @author Birdy
 *
 */
public class IdentityActivationFunction implements ActivationFunction {

	@Override
	public void forward(MathMatrix input, MathMatrix output) {
		output.copyMatrix(input, false);
	}

	@Override
	public void forward(MathVector input, MathVector output) {
		output.copyVector(input);
	}

	@Override
	public void backward(MathMatrix input, MathMatrix error, MathMatrix output) {
		output.copyMatrix(error, false);
	}

	@Override
	public void backward(MathVector input, MathVector error, MathVector output) {
		output.copyVector(error);
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
		return "IdentityActivationFunction()";
	}

}
