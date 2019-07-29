package com.jstarcraft.ai.evaluate;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * 面向排名预测的评估器
 * 
 * @author Birdy
 *
 */
public abstract class RankingEvaluator extends AbstractEvaluator<IntSet, IntList> {

	/** 大小 */
	protected int size;

	protected RankingEvaluator(int size) {
		this.size = size;
	}

	@Override
	protected int count(IntSet checkCollection, IntList rankList) {
		return 1;
	}

}
