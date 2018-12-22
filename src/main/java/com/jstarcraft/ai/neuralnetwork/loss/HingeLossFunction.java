package com.jstarcraft.ai.neuralnetwork.loss;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;

/**
 * Hinge目标函数
 * 
 * <pre></pre>
 * 
 * @author Birdy
 *
 */
public class HingeLossFunction implements LossFunction {

	@Override
	public float computeScore(MathMatrix tests, MathMatrix trains, MathMatrix masks) {
		/*
		 * y_hat is -1 or 1 hinge loss is max(0,1-y_hat*y)
		 */
		float score = 0F;
		for (MatrixScalar term : trains) {
			score += (1F - term.getValue() * tests.getValue(term.getRow(), term.getColumn()));
		}
		// TODO 暂时不处理masks
		return score;
	}

	@Override
	public void computeGradient(MathMatrix tests, MathMatrix trains, MathMatrix masks, MathMatrix gradients) {
		/*
		 * y_hat is -1 or 1 hinge loss is max(0,1-y_hat*y)
		 */
		/*
		 * bit mask is 0 if 1-sigma(y*yhat) is neg bit mask is 1 if 1-sigma(y*yhat) is
		 * +ve
		 */
		gradients.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = scalar.getValue();
			value = 1F - value * tests.getValue(row, column);
			value = value > 0F ? 1F : 0F;
			value = -tests.getValue(row, column) * value;
			scalar.setValue(value);
		});
		// TODO 暂时不处理masks
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
		return "HingeLossFunction()";
	}

}
