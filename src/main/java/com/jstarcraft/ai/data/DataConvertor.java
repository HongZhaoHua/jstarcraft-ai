package com.jstarcraft.ai.data;

import com.jstarcraft.ai.data.accessor.InstanceAccessor;

/**
 * 数据转换器
 * 
 * <pre>
 * 负责将某种数据格式转换到数据模型{@link InstanceAccessor}中.
 * </pre>
 * 
 * @author Birdy
 *
 */
public interface DataConvertor {

	/**
	 * 将数据转换到指定特征中.
	 * 
	 * @param path
	 * @param model
	 * @return
	 */
	int convert(DataSpace space);

	String getName();

}
