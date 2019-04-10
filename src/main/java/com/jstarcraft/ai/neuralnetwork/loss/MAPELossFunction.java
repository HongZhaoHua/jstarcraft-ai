package com.jstarcraft.ai.neuralnetwork.loss;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;

/**
 * MAP目标函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class MAPELossFunction implements LossFunction {

	@Override
	public float computeScore(MathMatrix tests, MathMatrix trains, MathMatrix masks) {
		float score = 0F;
		float scale = 100F / trains.getColumnSize();
		for (MatrixScalar term : trains) {
			double value = term.getValue();
			double mark = tests.getValue(term.getRow(), term.getColumn());
			value = (value - mark) / mark;
			value = Math.abs(value) * scale;
			score += value;
		}
		// TODO 暂时不处理masks
		return score;
	}

	@Override
	public void computeGradient(MathMatrix tests, MathMatrix trains, MathMatrix masks, MathMatrix gradients) {
		float scale = -100F / trains.getColumnSize();
		gradients.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = trains.getValue(row, column);
			float mark = tests.getValue(row, column);
			value = mark - value;
			value = value < 0F ? -1F : (value > 0F ? 1F : 0F);
			value = value / Math.abs(mark) * scale;
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
		return "MAPELossFunction()";
	}

}
