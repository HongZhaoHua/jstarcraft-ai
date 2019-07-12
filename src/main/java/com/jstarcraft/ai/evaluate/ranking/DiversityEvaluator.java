package com.jstarcraft.ai.evaluate.ranking;

import com.jstarcraft.ai.evaluate.RankingEvaluator;
import com.jstarcraft.ai.math.structure.matrix.SymmetryMatrix;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * 多样性评估器
 *
 * @author Birdy
 */
public class DiversityEvaluator extends RankingEvaluator {

	private SymmetryMatrix similarityMatrix;

	public DiversityEvaluator(int size, SymmetryMatrix similarityMatrix) {
		super(size);
		this.similarityMatrix = similarityMatrix;
	}

	@Override
	protected float measure(IntSet checkCollection, IntList rankList) {
		if (rankList.size() > size) {
		    rankList = rankList.subList(0, size);
		}
		float diversity = 0F;
		int size = rankList.size();
		for (int indexOut = 0; indexOut < size; indexOut++) {
			for (int indexIn = indexOut + 1; indexIn < size; indexIn++) {
				int itemOut = rankList.get(indexOut);
				int itemIn = rankList.get(indexIn);
				diversity += 1F - similarityMatrix.getValue(itemOut, itemIn);
				diversity += 1F - similarityMatrix.getValue(itemIn, itemOut);
			}
		}
		return diversity / (size * (size - 1));
	}

}
