package com.jstarcraft.ai.data;

/**
 * 连续访问器
 * 
 * @author Birdy
 *
 */
public interface QuantityAccessor {

	/**
	 * 访问特征
	 * 
	 * @param index
	 * @param value
	 */
	void accessorFeature(int index, float value);

}
