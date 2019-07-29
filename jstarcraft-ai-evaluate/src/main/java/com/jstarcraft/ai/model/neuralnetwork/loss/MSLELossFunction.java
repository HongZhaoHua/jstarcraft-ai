package com.jstarcraft.ai.model.neuralnetwork.loss;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;

/**
 * Mean Squared Logarithmic Error目标函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class MSLELossFunction implements LossFunction {

	@Override
	public float computeScore(MathMatrix tests, MathMatrix trains, MathMatrix masks) {
		float score = 0F;
		float scale = trains.getColumnSize();
		for (MatrixScalar term : trains) {
			float value = term.getValue();
			value = (float) (FastMath.log((value + 1F) / (tests.getValue(term.getRow(), term.getColumn()) + 1F)));
			value = value * value / scale;
			score += value;
		}
		// TODO 暂时不处理masks
		return score;
	}

	@Override
	public void computeGradient(MathMatrix tests, MathMatrix trains, MathMatrix masks, MathMatrix gradients) {
		float scale = 2F / trains.getColumnSize();
		gradients.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = trains.getValue(row, column);
			float ratio = (float) (FastMath.log((value + 1F) / (tests.getValue(row, column) + 1F)));
			value = scale / (value + 1F);
			scalar.setValue(value * ratio);
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
		return "MSLELossFunction()";
	}

}
