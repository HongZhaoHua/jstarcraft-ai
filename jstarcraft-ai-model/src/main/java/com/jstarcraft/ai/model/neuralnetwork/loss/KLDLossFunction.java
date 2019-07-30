package com.jstarcraft.ai.model.neuralnetwork.loss;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.MathUtility;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;

/**
 * Binary XENT目标函数
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class KLDLossFunction implements LossFunction {

	@Override
	public float computeScore(MathMatrix tests, MathMatrix trains, MathMatrix masks) {
		float score = 0F;
		for (MatrixScalar term : trains) {
			float value = term.getValue();
			value = value < MathUtility.EPSILON ? MathUtility.EPSILON : (value > 1F ? 1F : value);
			float mark = tests.getValue(term.getRow(), term.getColumn());
			mark = mark < MathUtility.EPSILON ? MathUtility.EPSILON : (mark > 1F ? 1F : mark);
			float ratio = (float) FastMath.log(mark / value);
			score += ratio * mark;
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
			value = -(tests.getValue(row, column) / value);
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
		return "KLDLossFunction()";
	}

}
