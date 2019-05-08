package com.jstarcraft.ai.data.converter;

import com.jstarcraft.ai.data.DataModule;

/**
 * 数据转换器
 * 
 * @author Birdy
 *
 */
public interface DataConverter<T> {

    /**
     * 转换
     * 
     * <pre>
     * 通过迭代器将指定数据转换到模块
     * </pre>
     * 
     * @param module
     * @param iterator
     * @param qualityMarkOrder
     * @param quantityMarkOrder
     * @param weightOrder
     * @return
     */
    int convert(DataModule module, T iterator, Integer qualityMarkOrder, Integer quantityMarkOrder, Integer weightOrder);

}
