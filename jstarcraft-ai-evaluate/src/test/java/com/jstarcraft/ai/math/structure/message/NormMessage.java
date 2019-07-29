package com.jstarcraft.ai.math.structure.message;

import com.jstarcraft.ai.math.structure.MathMessage;
import com.jstarcraft.ai.math.structure.message.AccumulationMessage;

/**
 * 范数消息
 * 
 * <pre>
 * 注意:
 * 获取范数的标准方法为Math.pow(value, 1D / power)
 * </pre>
 * 
 * @author Birdy
 *
 */
public class NormMessage implements AccumulationMessage<Float> {

	private int power;

	private float value = 0F;

	public NormMessage(int power) {
		this.power = power;
	}

	@Override
	public void attach(MathMessage message) {
		value += NormMessage.class.cast(message).value;
	}

	@Override
	public MathMessage detach() {
		MathMessage message = new NormMessage(power);
		return message;
	}

	@Override
	public void accumulateValue(float value) {
		this.value += Math.pow(Math.abs(value), power);
	}

	@Override
	public Float getValue() {
		return value;
	}

	public int getPower() {
		return power;
	}

}