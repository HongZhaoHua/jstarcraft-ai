package com.jstarcraft.ai.search;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

/**
 * PageRank
 * 
 * @author Birdy
 *
 */
public class PageRank {

	/** 阻尼系数(原始性调整) */
	private final static float defaultAlpha = 0.8F;

	/** 收敛系数 */
	private final static float defaultEpsilon = 0.001F;

	private int dimension;

	private MathMatrix hMatrix;

	private float[] scores;

	public PageRank(int dimension, MathMatrix hMatrix) {
		this.dimension = dimension;
		this.hMatrix = hMatrix;
		this.scores = new float[dimension];
	}

	public void findPageRank() {
		findPageRank(defaultAlpha, defaultEpsilon);
	}

	public void findPageRank(float alpha, float epsilon) {
		// 初始化得分
		float invert = 1F / dimension;
		for (int index = 0; index < dimension; index++) {
			scores[index] = invert;
		}

		// 悬孤节点
		float[][] dNodes = getDanglingNodes();

		// 随机性调整,随机转跳.
		float tNodes = (1F - alpha) * invert;

		// Replace the H matrix with the G matrix
		hMatrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			scalar.setValue(alpha * scalar.getValue() + dNodes[scalar.getRow()][scalar.getColumn()] + tNodes);
		});

		// 判断是否收敛
		float error = 1F;
		while (error >= epsilon) {
			error = 0F;
			for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
				float score = 0F;
				for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
					score += scores[rowIndex] * hMatrix.getValue(rowIndex, columnIndex);
				}
				error += Math.abs(score - scores[columnIndex]);
				scores[columnIndex] = score;
			}
		}
	}

	float error = 1F;
	// TODO 考虑重构为支持稀疏
	private float[][] getDanglingNodes() {
		MathMatrix matrixH = hMatrix;

		int n = matrixH.getColumnSize();

		float inv_n = 1F / n;

		// The dangling node vector
		int[] dangling = new int[n];
		for (int index = 0; index < n; index++) {
			if (matrixH.getRowVector(index).getSum(false) == 0) {
				dangling[index] = 1;
			}
		}

		float[][] dNodes = new float[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {

				if (dangling[i] == 0) {
					dNodes[i][j] = 0;
				} else {
					dNodes[i][j] = defaultAlpha * inv_n;
				}
			}
		}

		return dNodes;
	}

	/**
	 * @return the pR
	 */
	public float getPageRank(int i) {
		return scores[i];
	}
}
