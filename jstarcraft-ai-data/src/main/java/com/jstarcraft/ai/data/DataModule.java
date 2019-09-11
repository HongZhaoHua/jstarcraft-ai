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
     * @param quantityFeatures
     * @param qualityMark
     * @param quantityMark
     * @param weight
     */
    void associateInstance(Int2IntSortedMap qualityFeatures, Int2FloatSortedMap quantityFeatures, int qualityMark, float quantityMark, float weight);

    /**
     * 关联实例
     * 
     * @param qualityFeatures
     * @param quantityFeatures
     * @param qualityMark
     * @param weight
     */
    default void associateInstance(Int2IntSortedMap qualityFeatures, Int2FloatSortedMap quantityFeatures, int qualityMark, float weight) {
        associateInstance(qualityFeatures, quantityFeatures, qualityMark, DataInstance.defaultFloat, weight);
    }

    /**
     * 关联实例
     * 
     * @param qualityFeatures
     * @param quantityFeatures
     * @param quantityMark
     * @param weight
     */
    default void associateInstance(Int2IntSortedMap qualityFeatures, Int2FloatSortedMap quantityFeatures, float quantityMark, float weight) {
        associateInstance(qualityFeatures, quantityFeatures, DataInstance.defaultInteger, quantityMark, weight);
    }

    /**
     * 关联实例
     * 
     * @param qualityFeatures
     * @param quantityFeatures
     * @param qualityMark
     */
    default void associateInstance(Int2IntSortedMap qualityFeatures, Int2FloatSortedMap quantityFeatures, int qualityMark) {
        associateInstance(qualityFeatures, quantityFeatures, qualityMark, DataInstance.defaultFloat, DataInstance.defaultWeight);
    }

    /**
     * 关联实例
     * 
     * @param qualityFeatures
     * @param quantityFeatures
     * @param quantityMark
     */
    default void associateInstance(Int2IntSortedMap qualityFeatures, Int2FloatSortedMap quantityFeatures, float quantityMark) {
        associateInstance(qualityFeatures, quantityFeatures, DataInstance.defaultInteger, quantityMark, DataInstance.defaultWeight);
    }

    /**
     * 关联实例
     * 
     * @param qualityFeatures
     * @param quantityFeatures
     */
    default void associateInstance(Int2IntSortedMap qualityFeatures, Int2FloatSortedMap quantityFeatures) {
        associateInstance(qualityFeatures, quantityFeatures, DataInstance.defaultInteger, DataInstance.defaultFloat, DataInstance.defaultWeight);
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
     * 获取指定外部维度的属性投影(true代表离散,false代表连续)
     * 
     * @param index
     * @return
     */
    Entry<Integer, KeyValue<String, Boolean>> getOuterKeyValue(int index);

    /**
     * 获取指定离散属性的内部维度
     * 
     * @param name
     * @return
     */
    int getQualityInner(String name);

    /**
     * 获取指定连续属性的内部维度
     * 
     * @param name
     * @return
     */
    int getQuantityInner(String name);

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
    int getQuantityOrder();

}
