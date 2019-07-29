package com.jstarcraft.ai.math.structure;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * 数学工厂
 * 
 * <pre>
 * 配合环境控制矩阵和向量对缓存的使用
 * </pre>
 * 
 * @author Birdy
 *
 */
public interface MathCache {

	MathMatrix makeMatrix(int rowSize, int columnSize);

	MathVector makeVector(int capacitySize);

}
