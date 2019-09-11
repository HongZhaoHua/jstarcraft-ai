package com.jstarcraft.ai.data;

/**
 * 定量访问器
 * 
 * @author Birdy
 *
 */
public interface QuantityAccessor {

    /**
     * 访问特征
     * 
     * @param dimension
     * @param value
     */
    void accessorFeature(int dimension, float value);

}
