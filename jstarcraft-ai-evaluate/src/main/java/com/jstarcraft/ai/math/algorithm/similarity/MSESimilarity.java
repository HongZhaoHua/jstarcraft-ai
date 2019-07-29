package com.jstarcraft.ai.math.algorithm.similarity;

import java.util.List;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.utility.Float2FloatKeyValue;

/**
 * Mean Square Error相似度
 * 
 * @author Birdy
 *
 */
public class MSESimilarity extends AbstractSimilarity {

	private float getSimilarity(int count, List<Float2FloatKeyValue> scoreList) {
		if (count == 0) {
			return Float.NaN;
		}
		float similarity = 0F;
		for (Float2FloatKeyValue term : scoreList) {
			float delta = term.getKey() - term.getValue();
			similarity += Math.pow(delta, 2);
		}
		return similarity / count;
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

	@Override
	public float getIdentical() {
		return 0F;
	}

}
