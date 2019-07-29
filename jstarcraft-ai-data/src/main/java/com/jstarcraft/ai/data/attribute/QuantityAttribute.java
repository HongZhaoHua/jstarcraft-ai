package com.jstarcraft.ai.data.attribute;

import com.jstarcraft.ai.data.DataAttribute;

/**
 * 连续属性
 * 
 * @author Birdy
 *
 * @param <T>
 */
public interface QuantityAttribute<T extends Number> extends DataAttribute<T> {

	/**
	 * 转换属性值
	 * 
	 * @param data
	 * @return
	 */
	float convertData(T data);

	float getMaximum();

	float getMinimum();

}
