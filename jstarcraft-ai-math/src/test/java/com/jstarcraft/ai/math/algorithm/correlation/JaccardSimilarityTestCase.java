package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.JaccardSimilarity;
import com.jstarcraft.ai.math.algorithm.correlation.Similarity;

public class JaccardSimilarityTestCase extends AbstractSimilarityTestCase {

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
		return new JaccardSimilarity();
	}

}
