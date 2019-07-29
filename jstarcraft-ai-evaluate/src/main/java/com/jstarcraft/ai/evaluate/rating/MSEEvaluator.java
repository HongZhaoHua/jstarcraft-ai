package com.jstarcraft.ai.evaluate.rating;

import java.util.Iterator;

import com.jstarcraft.ai.evaluate.RatingEvaluator;

import it.unimi.dsi.fastutil.floats.FloatList;

/**
 * 均方误差
 * 
 * <pre>
 * MSE = Mean Squared Error
 * </pre>
 *
 * @author Birdy
 */
public class MSEEvaluator extends RatingEvaluator {

	@Override
	protected float measure(FloatList checkCollection, FloatList rateList) {
		float value = 0F;
		Iterator<Float> iterator = checkCollection.iterator();
		for (float estimate : rateList) {
			float score = iterator.next();
			value += Math.pow(score - estimate, 2);
		}
		return value;
	}

}
