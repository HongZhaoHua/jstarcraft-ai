package com.jstarcraft.ai.model.supervised;

import com.jstarcraft.ai.data.DataInstance;

/**
 * 预测器
 * 
 * @author Birdy
 *
 */
public interface Predictor {

	/**
	 * 预测
	 * 
	 * @param instance
	 * @param contexts
	 */
	void predict(DataInstance instance, DataInstance... contexts);

}
