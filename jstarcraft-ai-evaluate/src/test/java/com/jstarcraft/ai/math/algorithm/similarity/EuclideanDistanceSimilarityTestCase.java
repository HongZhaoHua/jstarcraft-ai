package com.jstarcraft.ai.math.algorithm.similarity;

public class EuclideanDistanceSimilarityTestCase extends AbstractSimilarityTestCase {

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
		return new EuclideanDistanceSimilarity();
	}

}
