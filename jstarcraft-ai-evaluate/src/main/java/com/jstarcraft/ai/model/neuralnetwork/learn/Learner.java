package com.jstarcraft.ai.model.neuralnetwork.learn;

import java.util.Map;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

/**
 * 学习器
 * 
 * @author Birdy
 *
 */
public interface Learner {

	/**
	 * 根据指定的梯度分配缓存(每次epoch调用)
	 * 
	 * @param numberOfInstances
	 * @param numberOfParameters
	 */
	void doCache(Map<String, MathMatrix> gradients);

	/**
	 * 根据梯度调整参数
	 * 
	 * @param gradients
	 * @param iteration
	 * @param epoch
	 */
	void learn(Map<String, MathMatrix> gradients, int iteration, int epoch);

}
