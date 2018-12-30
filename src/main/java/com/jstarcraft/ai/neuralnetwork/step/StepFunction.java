package com.jstarcraft.ai.neuralnetwork.step;

import java.util.Map;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

/**
 * 步长函数
 * 
 * @author Birdy
 *
 */
public interface StepFunction {

	void step(float step, Map<String, MathMatrix> directions, Map<String, MathMatrix> parameters);

}
