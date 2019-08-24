package com.jstarcraft.ai.math.algorithm.correlation;

import java.util.List;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Pearson Correlation Coefficient相似度
 * 
 * @author Birdy
 *
 */
public class PCCSimilarity extends AbstractSimilarity {

	private float getSimilarity(int count, List<Float2FloatKeyValue> scoreList) {
		// compute similarity
		if (count < 2) {
			return Float.NaN;
		}
		float leftMean = 0F;
		float rightMean = 0F;
		for (Float2FloatKeyValue term : scoreList) {
			leftMean += term.getKey();
			rightMean += term.getValue();
		}
		leftMean /= count;
		rightMean /= count;
		float sum = 0F, leftPower = 0F, rightPower = 0F;
		for (Float2FloatKeyValue term : scoreList) {
			float leftDelta = term.getKey() - leftMean;
			float rightDelta = term.getValue() - rightMean;
			sum += leftDelta * rightDelta;
			leftPower += leftDelta * leftDelta;
			rightPower += rightDelta * rightDelta;
		}
		return (float) (sum / (Math.sqrt(leftPower) * Math.sqrt(rightPower)));
	}

	@Override
	public float getCorrelation(MathVector leftVector, MathVector rightVector, float scale) {
		// compute similarity
		List<Float2FloatKeyValue> scoreList = getScoreList(leftVector, rightVector);
		int count = scoreList.size();
		float similarity = getSimilarity(count, scoreList);
		// shrink to account for vector size
		if (!Double.isNaN(similarity)) {
			if (scale > 0) {
				similarity *= count / (count + scale);
			}
		}
		return similarity;
	}

}
