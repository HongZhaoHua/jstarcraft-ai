package com.jstarcraft.ai.evaluate.rating;

import java.util.Iterator;

import com.jstarcraft.ai.evaluate.RatingEvaluator;

import it.unimi.dsi.fastutil.floats.FloatList;

/**
 * 平均相对误差评估器
 * 
 * <pre>
 * MPE = Mean Prediction  Error
 * </pre>
 *
 * @author Birdy
 */
public class MPEEvaluator extends RatingEvaluator {

	private float mpe;

	public MPEEvaluator(float mpe) {
		this.mpe = mpe;
	}

	@Override
	protected float measure(FloatList checkCollection, FloatList rateList) {
		float value = 0F;
		Iterator<Float> iterator = checkCollection.iterator();
		for (float estimate : rateList) {
			float score = iterator.next();
			if (Math.abs(score - estimate) > mpe) {
				value++;
			}
		}
		return value;
	}

}
