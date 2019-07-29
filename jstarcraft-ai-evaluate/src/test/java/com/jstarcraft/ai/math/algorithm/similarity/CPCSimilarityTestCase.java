package com.jstarcraft.ai.math.algorithm.similarity;

import com.jstarcraft.ai.math.algorithm.similarity.CPCSimilarity;
import com.jstarcraft.ai.math.algorithm.similarity.Similarity;

public class CPCSimilarityTestCase extends AbstractSimilarityTestCase {

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
		return new CPCSimilarity();
	}

}
