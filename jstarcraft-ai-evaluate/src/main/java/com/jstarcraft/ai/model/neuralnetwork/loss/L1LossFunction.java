package com.jstarcraft.ai.model.neuralnetwork.loss;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;

/**
 * L1目标函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class L1LossFunction implements LossFunction {

	@Override
	public float computeScore(MathMatrix tests, MathMatrix trains, MathMatrix masks) {
		float score = 0F;
		for (MatrixScalar term : trains) {
			float value = term.getValue();
			value = value - tests.getValue(term.getRow(), term.getColumn());
			score += Math.abs(value);
		}
		// TODO 暂时不处理masks
		return score;
	}

	@Override
	public void computeGradient(MathMatrix tests, MathMatrix trains, MathMatrix masks, MathMatrix gradients) {
		gradients.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = trains.getValue(row, column);
			value = value - tests.getValue(row, column);
			value = value < 0F ? -1F : (value > 0F ? 1F : 0F);
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
		return "L1LossFunction";
	}

}
