package com.jstarcraft.ai.model.neuralnetwork.loss;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

/**
 * 损失函数
 * 
 * @author Birdy
 *
 */
public interface LossFunction {

	default void doCache(MathMatrix tests, MathMatrix trains) {
	}

	/**
	 * 计算得分
	 * 
	 * @param tests
	 * @param trains
	 * @param masks
	 * @return
	 */
	float computeScore(MathMatrix tests, MathMatrix trains, MathMatrix masks);

	/**
	 * 计算梯度
	 * 
	 * @param tests
	 * @param trains
	 * @param masks
	 * @param gradients
	 */
	void computeGradient(MathMatrix tests, MathMatrix trains, MathMatrix masks, MathMatrix gradients);

}
