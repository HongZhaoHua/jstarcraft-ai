package com.jstarcraft.ai.evaluate;

import com.jstarcraft.core.utility.Integer2FloatKeyValue;

/**
 * 评估器
 * 
 * @author Birdy
 *
 */
public interface Evaluator<L, R> {

    /**
     * 评估
     * 
     * @param collection
     * @param list
     * @return
     */
    Integer2FloatKeyValue evaluate(L collection, R list);

}
