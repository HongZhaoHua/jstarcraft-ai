package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.DiceCoefficientSimilarity;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;

public class DiceCoefficientSimilarityTestCase extends AbstractSimilarityTestCase {

	@Override
	protected boolean checkCorrelation(float correlation) {
		return correlation < 1.00001F;
	}

	@Override
	protected float getIdentical() {
		return 1F;
	}
	
	@Override
	protected Correlation getSimilarity() {
		return new DiceCoefficientSimilarity();
	}

}
