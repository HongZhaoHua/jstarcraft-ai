package com.jstarcraft.ai.data.processor;

import com.jstarcraft.ai.data.accessor.DataInstance;

/**
 * 数据选择器
 * 
 * @author Birdy
 *
 */
public interface DataSelector {

	boolean select(DataInstance instance);

}
