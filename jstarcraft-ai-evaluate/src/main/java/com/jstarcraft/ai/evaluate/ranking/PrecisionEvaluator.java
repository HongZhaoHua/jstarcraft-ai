package com.jstarcraft.ai.evaluate.ranking;

import com.jstarcraft.ai.evaluate.RankingEvaluator;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * 精确度评估器
 * 
 * <pre>
 * https://en.wikipedia.org/wiki/Precision_and_recall
 * </pre>
 * 
 * @author Birdy
 */
public class PrecisionEvaluator extends RankingEvaluator {

    public PrecisionEvaluator(int size) {
        super(size);
    }

    @Override
    protected float measure(IntSet checkCollection, IntList rankList) {
        if (rankList.size() > size) {
            rankList = rankList.subList(0, size);
        }
        int count = 0;
        for (int itemIndex : rankList) {
            if (checkCollection.contains(itemIndex)) {
                count++;
            }
        }
        return count / (size + 0F);
    }

}
