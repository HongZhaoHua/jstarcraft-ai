package com.jstarcraft.ai.math.structure.message;

import com.jstarcraft.ai.math.structure.MathMessage;

/**
 * 边界消息
 * 
 * @author Birdy
 *
 */
public class NonZeroMeanMessage implements AccumulationMessage<Float> {

	private float sum = 0F;

	private int count = 0;

	@Override
	public void attach(MathMessage message) {
		NonZeroMeanMessage that = NonZeroMeanMessage.class.cast(message);
		this.sum += that.sum;
		this.count += that.count;
	}

	@Override
	public MathMessage detach() {
		MathMessage message = new NonZeroMeanMessage();
		return message;
	}

	@Override
	public void accumulateValue(float value) {
		if (value != 0F) {
			sum += value;
			count++;
		}
	}

	@Override
	public Float getValue() {
		return sum / count;
	}

}