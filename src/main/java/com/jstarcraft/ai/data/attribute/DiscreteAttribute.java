package com.jstarcraft.ai.data.attribute;

import com.jstarcraft.ai.data.DataAttribute;

/**
 * 离散属性
 * @author Birdy
 *
 * @param <T>
 */
public interface DiscreteAttribute<T extends Comparable<T>> extends DataAttribute<T> {

	/**
	 * 转换属性值
	 * 
	 * @param value
	 * @return
	 */
	int convertValue(T value);

	int getSize();

}