package com.jstarcraft.ai.math.structure;

import java.util.concurrent.atomic.AtomicInteger;

import com.jstarcraft.ai.math.structure.message.AccumulationMessage;

public class MockMessage implements AccumulationMessage<Float> {

    private AtomicInteger attachTimes = new AtomicInteger();

    private AtomicInteger detachTimes = new AtomicInteger();

    private float value = 0F;

    @Override
    public void attach(MathMessage message) {
        attachTimes.incrementAndGet();
        value += MockMessage.class.cast(message).value;
    }

    @Override
    public MathMessage detach() {
        detachTimes.incrementAndGet();
        MathMessage message = new MockMessage();
        return message;
    }

    @Override
    public synchronized void accumulateValue(float value) {
        this.value += value;
    }

    @Override
    public Float getValue() {
        return value;
    }

    public int getAttachTimes() {
        return attachTimes.get();
    }

    public int getDetachTimes() {
        return detachTimes.get();
    }

}