package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.ManhattanDistanceSimilarity;
import com.jstarcraft.ai.math.algorithm.correlation.Similarity;

public class ManhattanDistanceSimilarityTestCase extends AbstractSimilarityTestCase {

	@Override
	protected boolean checkCorrelation(float correlation) {
		return correlation < 1.00001F;
	}

	@Override
	protected float getIdentical() {
		return 1F;
	}

	@Override
	protected Similarity getSimilarity() {
		return new ManhattanDistanceSimilarity();
	}

}
