package com.jstarcraft.ai.math.structure;

/**
 * 数学消息
 * 
 * @author Birdy
 *
 */
// TODO 可能会定义为一种辅助结构,重构到core.utility
public interface MathMessage<T> {

	/**
	 * 合并
	 * 
	 * @param message
	 */
	void attach(MathMessage<T> message);

	/**
	 * 分离
	 * 
	 * @return
	 */
	MathMessage<T> detach();

	/**
	 * 累计值
	 * 
	 * @param value
	 */
	void accumulateValue(float value);

	/**
	 * 获取值
	 * 
	 * @return
	 */
	T getValue();

}
