package com.jstarcraft.ai.evaluate.rating;

import java.util.Iterator;

import com.jstarcraft.ai.evaluate.RatingEvaluator;

import it.unimi.dsi.fastutil.floats.FloatList;

/**
 * 平均绝对误差评估器
 * 
 * <pre>
 * MAE = Mean Absolute Error
 * </pre>
 *
 * @author Birdy
 */
public class MAEEvaluator extends RatingEvaluator {

    public MAEEvaluator(float minimum, float maximum) {
        super(minimum, maximum);
    }

    @Override
    protected float measure(FloatList checkCollection, FloatList rateList) {
        float value = 0F;
        Iterator<Float> iterator = checkCollection.iterator();
        for (float estimate : rateList) {
            float score = iterator.next();
            if (estimate > maximum) {
                estimate = maximum;
            }
            if (estimate < minimum) {
                estimate = minimum;
            }
            value += Math.abs(score - estimate);
        }
        return value;
    }

}
