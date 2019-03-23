package com.jstarcraft.ai.data;

/**
 * 离散访问器
 * 
 * @author Birdy
 *
 */
public interface QualityAccessor {

	/**
	 * 访问特征
	 * 
	 * @param index
	 * @param value
	 */
	void accessorFeature(int index, int value);

}
