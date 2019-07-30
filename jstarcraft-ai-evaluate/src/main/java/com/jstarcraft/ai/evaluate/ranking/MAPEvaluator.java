package com.jstarcraft.ai.evaluate.ranking;

import com.jstarcraft.ai.evaluate.RankingEvaluator;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * 平均准确率均值评估器
 * 
 * <pre>
 * MAP = Mean Average Precision
 * https://en.wikipedia.org/wiki/Information_retrieval
 * https://www.kaggle.com/wiki/MeanAveragePrecision
 * </pre>
 * 
 * @author Birdy
 */
public class MAPEvaluator extends RankingEvaluator {

	public MAPEvaluator(int size) {
		super(size);
	}

	@Override
	protected float measure(IntSet checkCollection, IntList rankList) {
		if (rankList.size() > size) {
		    rankList = rankList.subList(0, size);
		}
		int count = 0;
		float map = 0F;
		for (int index = 0; index < rankList.size(); index++) {
			int itemIndex = rankList.get(index);
			if (checkCollection.contains(itemIndex)) {
				count++;
				map += 1F * count / (index + 1);
			}
		}
		return map / (checkCollection.size() < rankList.size() ? checkCollection.size() : rankList.size());
	}

}
