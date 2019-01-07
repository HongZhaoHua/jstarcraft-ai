package com.jstarcraft.ai.math.structure;

/**
 * 数学迭代器
 * 
 * @author Birdy
 *
 */
public interface CellIterator<T extends MathScalar> extends MathIterator<T> {

	/**
	 * 设置所有单元的值
	 * 
	 * @param value
	 * @return
	 */
	CellIterator<T> setValues(T value);

}
