package com.jstarcraft.ai.evaluate;

import it.unimi.dsi.fastutil.floats.FloatList;

/**
 * 面向评分预测的评估器
 * 
 * @author Birdy
 *
 */
public abstract class RatingEvaluator extends AbstractEvaluator<FloatList, FloatList> {

	@Override
	protected int count(FloatList checkCollection, FloatList rateList) {
		return checkCollection.size();
	}

}
