package com.jstarcraft.ai.data;

import java.util.Map.Entry;

import com.jstarcraft.core.utility.KeyValue;

import it.unimi.dsi.fastutil.ints.Int2FloatSortedMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;

/**
 * 数据模型
 * 
 * <pre>
 * 模型由实例组成,每个实例由离散特征和连续特征组成.
 * </pre>
 * 
 * @author Birdy
 *
 */
public interface DataModule extends Iterable<DataInstance> {

	/**
	 * 关联实例
	 * 
	 * @param qualityFeatures
	 * @param continuousFeatures
	 * @param qualityMark
	 * @param continuousMark
	 */
	void associateInstance(Int2IntSortedMap qualityFeatures, Int2FloatSortedMap continuousFeatures, int qualityMark, float continuousMark);

	/**
	 * 关联实例
	 * 
	 * @param qualityFeatures
	 * @param continuousFeatures
	 * @param qualityMark
	 */
	default void associateInstance(Int2IntSortedMap qualityFeatures, Int2FloatSortedMap continuousFeatures, int qualityMark) {
		associateInstance(qualityFeatures, continuousFeatures, qualityMark, DataInstance.defaultFloat);
	}

	/**
	 * 关联实例
	 * 
	 * @param qualityFeatures
	 * @param continuousFeatures
	 * @param continuousMark
	 */
	default void associateInstance(Int2IntSortedMap qualityFeatures, Int2FloatSortedMap continuousFeatures, float continuousMark) {
		associateInstance(qualityFeatures, continuousFeatures, DataInstance.defaultInteger, continuousMark);
	}

	/**
	 * 关联实例
	 * 
	 * @param qualityFeatures
	 * @param continuousFeatures
	 */
	default void associateInstance(Int2IntSortedMap qualityFeatures, Int2FloatSortedMap continuousFeatures) {
		associateInstance(qualityFeatures, continuousFeatures, DataInstance.defaultInteger, DataInstance.defaultFloat);
	}

	/**
	 * 获取实例
	 * 
	 * @param cursor
	 * @return
	 */
	DataInstance getInstance(int cursor);

	/**
	 * 获取大小
	 * 
	 * @return
	 */
	int getSize();

	/**
	 * 获取指定外部索引的属性投影(true代表离散,false代表连续)
	 * 
	 * @param index
	 * @return
	 */
	Entry<Integer, KeyValue<String, Boolean>> getOuterKeyValue(int index);

	/**
	 * 获取指定离散属性的内部索引
	 * 
	 * @param name
	 * @return
	 */
	int getQualityInner(String name);

	/**
	 * 获取指定连续属性的内部索引
	 * 
	 * @param name
	 * @return
	 */
	int getContinuousInner(String name);

	/**
	 * 获取定性秩
	 * 
	 * @return
	 */
	int getQualityOrder();

	/**
	 * 获取连续秩
	 * 
	 * @return
	 */
	int getContinuousOrder();

}
