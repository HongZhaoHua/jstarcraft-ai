package com.jstarcraft.ai.neuralnetwork;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

/**
 * 缓存工厂
 * 
 * @author Birdy
 *
 */
public interface MatrixFactory {

	MathMatrix makeCache(int rowSize, int columnSize);

}
