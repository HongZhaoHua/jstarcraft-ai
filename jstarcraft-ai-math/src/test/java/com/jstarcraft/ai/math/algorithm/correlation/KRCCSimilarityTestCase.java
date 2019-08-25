package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.KRCCSimilarity;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;

public class KRCCSimilarityTestCase extends AbstractSimilarityTestCase {

	@Override
	protected boolean checkCorrelation(float correlation) {
		return correlation < 1.00001F;
	}

	@Override
	protected float getIdentical() {
		return 1F;
	}

	@Override
	protected Correlation getCorrelation() {
		return new KRCCSimilarity();
	}

}
