package com.jstarcraft.ai.evaluate;

import it.unimi.dsi.fastutil.floats.FloatList;

/**
 * 面向评分预测的评估器
 * 
 * @author Birdy
 *
 */
public abstract class RatingEvaluator extends AbstractEvaluator<FloatList, FloatList> {

    /** 大小 */
    protected float minimum, maximum;

    protected RatingEvaluator(float minimum, float maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    protected int count(FloatList checkCollection, FloatList rateList) {
        return checkCollection.size();
    }

}
