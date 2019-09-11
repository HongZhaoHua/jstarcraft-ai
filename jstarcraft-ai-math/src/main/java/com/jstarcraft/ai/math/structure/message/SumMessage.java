package com.jstarcraft.ai.math.structure.message;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.MathMessage;
import com.jstarcraft.ai.math.structure.message.AccumulationMessage;

/**
 * 总和消息
 * 
 * @author Birdy
 *
 */
public class SumMessage implements AccumulationMessage<Float> {

    private boolean absolute;

    private float value = 0F;

    public SumMessage(boolean absolute) {
        this.absolute = absolute;
    }

    @Override
    public void attach(MathMessage message) {
        value += SumMessage.class.cast(message).value;
    }

    @Override
    public MathMessage detach() {
        MathMessage message = new SumMessage(absolute);
        return message;
    }

    @Override
    public void accumulateValue(float value) {
        if (absolute) {
            value = FastMath.abs(value);
        }
        this.value += value;
    }

    @Override
    public Float getValue() {
        return value;
    }

}
