package com.jstarcraft.ai.evaluate.ranking;

import java.util.HashSet;
import java.util.Set;

import com.jstarcraft.ai.evaluate.RankingEvaluator;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * ROC曲线下的面积评估器
 * 
 * <pre>
 * AUC = Area Under roc Curve(ROC曲线下面积)
 * http://www.cnblogs.com/lixiaolun/p/4053499.html
 * </pre>
 *
 * @author Birdy
 */
public class AUCEvaluator extends RankingEvaluator {

	public AUCEvaluator(int size) {
		super(size);
	}

	@Override
	protected float measure(IntSet checkCollection, IntList rankList) {
		// 推荐物品集合(大小不能超过TopN)
		int evaluateSize = rankList.size();
		if (evaluateSize > size) {
		    rankList = rankList.subList(0, size);
		}
		int hitCount = 0, missCount = 0;
		Set<Integer> recommendItems = new HashSet<>();
		for (int itemIndex : rankList) {
			recommendItems.add(itemIndex);
			if (checkCollection.contains(itemIndex)) {
				hitCount++;
			} else {
				missCount++;
			}
		}

		int evaluateSum = (checkCollection.size() + evaluateSize - rankList.size() - hitCount) * hitCount;
		if (evaluateSum == 0) {
			return 0.5F;
		}
		int hitSum = 0;
		hitCount = 0;
		for (Integer itemIndex : checkCollection) {
			if (!recommendItems.contains(itemIndex)) {
				hitSum += hitCount;
			} else {
				hitCount++;
			}
		}
		hitSum += hitCount * (evaluateSize - missCount);
		return (hitSum + 0F) / evaluateSum;
	}

}