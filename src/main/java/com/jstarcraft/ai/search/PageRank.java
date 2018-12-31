package com.jstarcraft.ai.search;

import java.util.Iterator;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;

/**
 * PageRank
 * 
 * @author Birdy
 *
 */
// TODO 考虑重构为工具?
public class PageRank {

	/** 阻尼系数(原始性调整) */
	private final static float defaultAlpha = 0.8F;

	/** 收敛系数 */
	private final static float defaultEpsilon = 0.001F;

	/** 维度 */
	private int dimension;

	/** 矩阵 */
	private MathMatrix matrix;

	/** 得分 */
	private float[] scores;

	/** 悬孤 */
	// TODO 考虑重构为int[]
	private boolean[] ganglers;

	public PageRank(int dimension, MathMatrix matrix) {
		this.dimension = dimension;
		this.matrix = matrix;
		this.scores = new float[dimension];
		this.ganglers = new boolean[dimension];
	}

	public void calculateScores() {
		calculateScores(defaultAlpha, defaultEpsilon);
	}

	public void calculateScores(float alpha, float epsilon) {
		// 随机性调整
		float stochasticity = 1F / dimension;
		// 原始性调整
		float primitivity = (1F - alpha) * stochasticity;
		// 初始化得分
		for (int index = 0; index < dimension; index++) {
			scores[index] = stochasticity;
		}
		for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
			MathVector vector = matrix.getRowVector(rowIndex);
			if (vector.getElementSize() == 0 || vector.getSum(false) == 0F) {
				ganglers[rowIndex] = true;
			} else {
				vector.scaleValues(alpha);
				vector.shiftValues(primitivity);
			}
		}

		// 判断是否收敛
		float error = 1F;
		while (error >= epsilon) {
			error = 0F;
			for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
				float score = 0F;
				Iterator<VectorScalar> iterator = matrix.getColumnVector(columnIndex).iterator();
				VectorScalar scalar = null;
				int index = -1;
				float value = 0F;
				if (iterator.hasNext()) {
					scalar = iterator.next();
					index = scalar.getIndex();
					value = scalar.getValue();
				}
				for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
					if (index == rowIndex) {
						// 判断是否为悬孤
						if (ganglers[rowIndex]) {
							score += scores[rowIndex] * stochasticity;
						} else {
							score += scores[rowIndex] * value;
						}
						if (iterator.hasNext()) {
							scalar = iterator.next();
							index = scalar.getIndex();
							value = scalar.getValue();
						} else {
							scalar = null;
							index = -1;
							value = 0F;
						}
					} else {
						// 判断是否为悬孤
						if (ganglers[rowIndex]) {
							score += scores[rowIndex] * stochasticity;
						} else {
							score += scores[rowIndex] * primitivity;
						}
					}
				}
				error += Math.abs(score - scores[columnIndex]);
				scores[columnIndex] = score;
			}
		}
	}

	/**
	 * 获取得分
	 * 
	 * @param index
	 * @return
	 */
	public float getScore(int index) {
		return scores[index];
	}
}
