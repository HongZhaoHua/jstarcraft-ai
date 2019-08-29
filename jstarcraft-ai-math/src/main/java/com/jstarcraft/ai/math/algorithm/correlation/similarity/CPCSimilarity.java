package com.jstarcraft.ai.math.algorithm.correlation.similarity;

import java.util.List;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractSimilarity;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;
import com.jstarcraft.ai.math.structure.matrix.SymmetryMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Constrained Pearson Correlation相似度
 * 
 * @author Birdy
 *
 */
public class CPCSimilarity extends AbstractSimilarity {

	private double median;

	@Override
	public SymmetryMatrix makeCorrelationMatrix(MathMatrix trainMatrix, boolean transpose, float scale) {
		float maximum = 0F;
		float minimum = 0F;
		for (MatrixScalar term : trainMatrix) {
			if (term.getValue() > maximum) {
				maximum = term.getValue();
			}
			if (term.getValue() < minimum) {
				minimum = term.getValue();
			}
		}
		median = (maximum + minimum) / 2;
		return super.makeCorrelationMatrix(trainMatrix, transpose, scale);
	}

	private float getCoefficient(int count, List<Float2FloatKeyValue> scoreList) {
		// compute similarity
		if (count == 0) {
			return Float.NaN;
		}
		double power = 0D, leftPower = 0D, rightPower = 0D;
		for (Float2FloatKeyValue term : scoreList) {
			double leftDelta = term.getKey() - median;
			double rightDelta = term.getValue() - median;
			power += leftDelta * rightDelta;
			leftPower += leftDelta * leftDelta;
			rightPower += rightDelta * rightDelta;
		}
		return (float) (power / Math.sqrt(leftPower * rightPower));
	}

	@Override
	public float getCoefficient(MathVector leftVector, MathVector rightVector, float scale) {
		// compute similarity
		List<Float2FloatKeyValue> scoreList = getScoreList(leftVector, rightVector);
		int count = scoreList.size();
		float similarity = getCoefficient(count, scoreList);
		// shrink to account for vector size
		if (!Double.isNaN(similarity)) {
			if (scale > 0) {
				similarity *= count / (count + scale);
			}
		}
		return similarity;
	}

}
