package com.jstarcraft.ai.evaluate.ranking;

import java.util.List;

import com.jstarcraft.ai.evaluate.RankingEvaluator;
import com.jstarcraft.ai.utility.Integer2FloatKeyValue;

import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * 召回率评估器
 * 
 * <pre>
 * https://en.wikipedia.org/wiki/Precision_and_recall
 * </pre>
 *
 * @author Birdy
 */
public class RecallEvaluator extends RankingEvaluator {

    public RecallEvaluator(int size) {
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
        return count / (checkCollection.size() + 0F);
    }

}
