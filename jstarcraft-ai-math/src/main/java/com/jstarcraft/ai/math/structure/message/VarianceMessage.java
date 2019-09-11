package com.jstarcraft.ai.math.structure.message;

import com.jstarcraft.ai.math.structure.MathMessage;
import com.jstarcraft.ai.math.structure.message.AccumulationMessage;

/**
 * 方差消息
 * 
 * <pre>
 * 注意:
 * 获取方差(variance)的标准方法为value/size
 * 获取标准差(standardDeviation)的标准方法为Math.sqrt(value/size)
 * </pre>
 * 
 * @author Birdy
 *
 */
public class VarianceMessage implements AccumulationMessage<Float> {

    private float mean;

    private float value = 0F;

    public VarianceMessage(float mean) {
        this.mean = mean;
    }

    @Override
    public void attach(MathMessage message) {
        value += VarianceMessage.class.cast(message).value;
    }

    @Override
    public MathMessage detach() {
        MathMessage message = new VarianceMessage(mean);
        return message;
    }

    @Override
    public void accumulateValue(float value) {
        value = value - mean;
        this.value += (value * value);
    }

    @Override
    public Float getValue() {
        return value;
    }

    public double getMean() {
        return mean;
    }

}