package com.jstarcraft.ai.data.converter;

import com.jstarcraft.ai.data.DataModule;

/**
 * 数据转换器
 * 
 * @author Birdy
 *
 */
public interface DataConverter<T> {

	int convert(DataModule module, T iterator, Integer qualityMarkOrder, Integer quantityMarkOrder, Integer weightOrder);

}
