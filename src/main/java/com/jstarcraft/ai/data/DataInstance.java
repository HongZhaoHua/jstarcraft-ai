package com.jstarcraft.ai.data;

/**
 * 数据实例
 * 
 * @author Birdy
 *
 */
public interface DataInstance {

	public final static int defaultInteger = -1;

	public final static float defaultFloat = Float.NaN;

	/**
	 * 设置游标
	 * 
	 * @param cursor
	 */
	void setCursor(int cursor);

	/**
	 * 获取游标
	 * 
	 * @return
	 */
	int getCursor();

	/**
	 * 获取指定索引的离散特征
	 * 
	 * @param index
	 * @return
	 */
	int getDiscreteFeature(int index);

	/**
	 * 获取指定索引的连续特征
	 * 
	 * @param index
	 * @return
	 */
	float getContinuousFeature(int index);

	/**
	 * 遍历离散特征
	 * 
	 * @param accessor
	 * @return
	 */
	DataInstance iterateDiscreteFeatures(DiscreteAccessor accessor);

	/**
	 * 遍历连续特征
	 * 
	 * @param accessor
	 * @return
	 */
	DataInstance iterateContinuousFeatures(ContinuousAccessor accessor);

	/**
	 * 获取离散标记
	 * 
	 * @return
	 */
	int getDiscreteMark();

	/**
	 * 获取连续标记
	 * 
	 * @return
	 */
	float getContinuousMark();

	/**
	 * 获取稀疏秩
	 * 
	 * @return
	 */
	int getDiscreteOrder();

	/**
	 * 获取连续秩
	 * 
	 * @return
	 */
	int getContinuousOrder();

}
