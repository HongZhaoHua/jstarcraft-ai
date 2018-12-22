package com.jstarcraft.ai.data;

/**
 * 数据实例
 * 
 * @author Birdy
 *
 */
public interface DataInstance {
	
	void setCursor(int cursor);
	
	int getCursor();

	/**
	 * 获取指定维度的离散特征
	 * 
	 * @param index
	 * @return
	 */
	int getDiscreteFeature(int index);

	/**
	 * 获取指定维度的连续特征
	 * 
	 * @param index
	 * @return
	 */
	public float getContinuousFeature(int index);

}
