package com.jstarcraft.ai.data;

/**
 * 数据数组
 * 
 * @author Birdy
 *
 */
public interface DataArray {

	/**
	 * 获取最大容量
	 * 
	 * @return
	 */
	public int getMaximumCapacity();

	/**
	 * 获取最小容量
	 * 
	 * @return
	 */
	public int getMinimumCapacity();

	/**
	 * 获取大小
	 * 
	 * @return
	 */
	public int getSize();

}
