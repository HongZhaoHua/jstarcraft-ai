package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.MSDDistance;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;

public class MSDDistanceTestCase extends AbstractSimilarityTestCase {

	@Override
	protected boolean checkCorrelation(float correlation) {
		return correlation >= 0F && correlation < Float.POSITIVE_INFINITY;
	}

	@Override
	protected float getIdentical() {
		return 0F;
	}

	// TODO 注意MSD与MSE相似度是计算两个向量的均方误差,范围是0-正无穷.且if (row == column) value = 0D;
	@Override
	protected Correlation getCorrelation() {
		return new MSDDistance();
	}

}
