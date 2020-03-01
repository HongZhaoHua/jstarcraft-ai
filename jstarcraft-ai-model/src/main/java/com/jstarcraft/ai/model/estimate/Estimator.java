package com.jstarcraft.ai.model.estimate;

import com.jstarcraft.ai.model.Updateable;
import com.jstarcraft.core.utility.KeyValue;

public interface Estimator<T> extends Updateable<KeyValue<T, Float>> {

    /**
     * 更新概率
     * 
     * @param data
     * @param weight
     */
    void updateProbability(T data, float weight);

    /**
     * 获取概率
     * 
     * @param data
     * @return
     */
    float getProbability(T data);

    @Override
    default void update(KeyValue<T, Float> keyValue) {
        updateProbability(keyValue.getKey(), keyValue.getValue());
    }

}
