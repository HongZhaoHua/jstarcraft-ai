package com.jstarcraft.ai.data;

/**
 * 定性访问器
 * 
 * @author Birdy
 *
 */
public interface QualityAccessor {

	/**
	 * 访问特征
	 * 
	 * @param dimension
	 * @param value
	 */
	void accessorFeature(int dimension, int value);

}
