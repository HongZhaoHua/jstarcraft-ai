package com.jstarcraft.ai.math.algorithm.similarity;

import java.util.Iterator;
import java.util.List;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.KeyValue;

/**
 * Kendall Rank Correlation相似度
 * 
 * @author Birdy
 *
 */
public class KRCCSimilarity extends AbstractSimilarity {

	private float getSimilarity(int count, List<KeyValue<Float, Float>> scoreList) {
		if (count < 2) {
			return Float.NaN;
		}
		float sum = 0F;
		Iterator<KeyValue<Float, Float>> iterator = scoreList.iterator();
		KeyValue<Float, Float> previousTerm = iterator.next();
		KeyValue<Float, Float> nextTerm = null;
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
	public float getCorrelation(MathVector leftVector, MathVector rightVector, float scale) {
		// compute similarity
		List<KeyValue<Float, Float>> scoreList = getScoreList(leftVector, rightVector);
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
