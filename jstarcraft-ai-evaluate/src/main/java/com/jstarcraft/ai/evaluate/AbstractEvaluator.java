package com.jstarcraft.ai.evaluate;

import com.jstarcraft.core.utility.Integer2FloatKeyValue;

/**
 * 抽象评估器
 * 
 * @author Birdy
 *
 */
public abstract class AbstractEvaluator<L, R> implements Evaluator<L, R> {

    /**
     * 统计
     * 
     * @param collection
     * @param list
     * @return
     */
    protected abstract int count(L collection, R list);

    /**
     * 测量
     * 
     * @param collection
     * @param list
     * @return
     */
    protected abstract float measure(L collection, R list);

    @Override
    public final Integer2FloatKeyValue evaluate(L collection, R list) {
        return new Integer2FloatKeyValue(count(collection, list), measure(collection, list));
    }

}
