package com.jstarcraft.ai.math.structure;

/**
 * 数学表单
 * 
 * @author Birdy
 *
 * @param <T>
 */
public interface MathTable<T> {

	/**
	 * 获取行的大小
	 * 
	 * @return
	 */
	int getRowSize();

	/**
	 * 获取列的大小
	 * 
	 * @return
	 */
	int getColumnSize();

	/**
	 * 获取指定索引标量的值
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	T getValue(int rowIndex, int columnIndex);

	/**
	 * 设置指定索引标量的值
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * @param value
	 */
	void setValue(int rowIndex, int columnIndex, T value);

	/**
	 * 获取方向(true为按行,false为按列)
	 * 
	 * @return
	 */
	boolean getOrientation();

}
