package com.jstarcraft.ai.data;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;

/**
 * 数据模型
 * 
 * @author Birdy
 *
 */
public interface DataModule {

	/**
	 * 关联实例
	 * 
	 * @param discreteFeatures
	 * @param continuousFeatures
	 */
	void associateInstance(Int2IntMap discreteFeatures, Int2FloatMap continuousFeatures);

	/**
	 * 获取实例
	 * 
	 * @param cursor
	 * @return
	 */
	DataInstance getInstance(int cursor);

	int getSize();

}
