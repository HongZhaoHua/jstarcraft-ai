package com.jstarcraft.ai.supervised;

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
	 * @return
	 */
	void predict(DataInstance instance);

}
