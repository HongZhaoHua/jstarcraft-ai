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
	int getQualityFeature(int index);

	/**
	 * 获取指定索引的连续特征
	 * 
	 * @param index
	 * @return
	 */
	float getQuantityFeature(int index);

	/**
	 * 遍历离散特征
	 * 
	 * @param accessor
	 * @return
	 */
	DataInstance iterateQualityFeatures(QualityAccessor accessor);

	/**
	 * 遍历连续特征
	 * 
	 * @param accessor
	 * @return
	 */
	DataInstance iterateQuantityFeatures(QuantityAccessor accessor);

	/**
	 * 获取离散标记
	 * 
	 * @return
	 */
	int getQualityMark();

	/**
	 * 获取连续标记
	 * 
	 * @return
	 */
	float getQuantityMark();

	/**
	 * 获取离散秩
	 * 
	 * @return
	 */
	int getQualityOrder();

	/**
	 * 获取连续秩
	 * 
	 * @return
	 */
	int getQuantityOrder();

}
