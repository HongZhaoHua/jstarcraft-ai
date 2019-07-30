package com.jstarcraft.ai.evaluate.ranking;

import com.jstarcraft.ai.evaluate.RankingEvaluator;
import com.jstarcraft.ai.math.structure.matrix.SparseMatrix;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * NoveltyEvaluator
 *
 * @author Birdy
 */
public class NoveltyEvaluator extends RankingEvaluator {

    private int numberOfUsers;

    private int[] itemCounts;

    public NoveltyEvaluator(int size, SparseMatrix dataMatrix) {
        super(size);
        // use the purchase counts of the train and test data set
        numberOfUsers = dataMatrix.getRowSize();
        int numberOfItems = dataMatrix.getColumnSize();
        itemCounts = new int[numberOfItems];
        for (int itemIndex = 0; itemIndex < numberOfItems; itemIndex++) {
            itemCounts[itemIndex] = dataMatrix.getColumnScope(itemIndex);
        }
    }

    /**
     * Evaluate on the test set with the the list of recommended items.
     *
     * @param testMatrix      the given test set
     * @param recommendedList the list of recommended items
     * @return evaluate result
     */
    @Override
    protected float measure(IntSet checkCollection, IntList rankList) {
        if (rankList.size() > size) {
            rankList = rankList.subList(0, size);
        }
        float sum = 0F;
        for (int rank : rankList) {
            int count = itemCounts[rank];
            if (count > 0) {
                float probability = ((float) count) / numberOfUsers;
                float entropy = (float) -Math.log(probability);
                sum += entropy;
            }
        }
        return (float) (sum / Math.log(2F));
    }

}
