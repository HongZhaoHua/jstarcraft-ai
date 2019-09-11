package com.jstarcraft.ai.math.structure.message;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.MathMessage;
import com.jstarcraft.ai.math.structure.message.AccumulationMessage;
import com.jstarcraft.core.utility.KeyValue;

/**
 * 边界消息
 * 
 * @author Birdy
 *
 */
public class BoundaryMessage implements AccumulationMessage<KeyValue<Float, Float>> {

    private boolean absolute;

    private float maximum = Float.NEGATIVE_INFINITY;

    private float minimum = Float.POSITIVE_INFINITY;

    public BoundaryMessage(boolean absolute) {
        this.absolute = absolute;
    }

    @Override
    public void attach(MathMessage message) {
        BoundaryMessage that = BoundaryMessage.class.cast(message);
        if (this.maximum < that.maximum) {
            this.maximum = that.maximum;
        }
        if (this.minimum > that.minimum) {
            this.minimum = that.minimum;
        }
    }

    @Override
    public MathMessage detach() {
        MathMessage message = new BoundaryMessage(absolute);
        return message;
    }

    @Override
    public void accumulateValue(float value) {
        if (absolute) {
            value = FastMath.abs(value);
        }
        if (maximum < value) {
            maximum = value;
        }
        if (minimum > value) {
            minimum = value;
        }
    }

    @Override
    public KeyValue<Float, Float> getValue() {
        return new KeyValue<>(minimum, maximum);
    }

    public boolean isAbsolute() {
        return absolute;
    }

}