package com.jstarcraft.ai.neuralnetwork.normalization;

import java.util.Map;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

/**
 * 标准器
 * 
 * @author Birdy
 *
 */
public interface Normalizer {

	public enum Mode {

		GLOBAL,

		LOCAL;

	}

	void normalize(Map<String, MathMatrix> gradients);

}
