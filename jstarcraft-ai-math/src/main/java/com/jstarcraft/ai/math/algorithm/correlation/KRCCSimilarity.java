package com.jstarcraft.ai.math.algorithm.correlation;

import java.util.Iterator;
import java.util.List;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Kendall Rank Correlation相似度
 * 
 * @author Birdy
 *
 */
public class KRCCSimilarity extends AbstractSimilarity {

	private float getCoefficient(int count, List<Float2FloatKeyValue> scoreList) {
		if (count < 2) {
			return Float.NaN;
		}
		float sum = 0F;
		Iterator<Float2FloatKeyValue> iterator = scoreList.iterator();
		Float2FloatKeyValue previousTerm = iterator.next();
		Float2FloatKeyValue nextTerm = null;
		while (iterator.hasNext()) {
			nextTerm = iterator.next();
			float leftDelta = previousTerm.getKey() - nextTerm.getKey();
			float rightDelta = previousTerm.getValue() - nextTerm.getValue();
			if (leftDelta * rightDelta < 0F) {
				sum += 1D;
			}
			previousTerm = nextTerm;
		}
		return 1F - 4F * sum / (count * (count - 1));
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
