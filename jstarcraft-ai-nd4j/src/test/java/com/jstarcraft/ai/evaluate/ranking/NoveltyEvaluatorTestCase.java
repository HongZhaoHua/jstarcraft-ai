package com.jstarcraft.ai.evaluate.ranking;

import com.jstarcraft.ai.evaluate.AbstractRankingEvaluatorTestCase;
import com.jstarcraft.ai.evaluate.Evaluator;
import com.jstarcraft.ai.math.structure.matrix.SparseMatrix;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

public class NoveltyEvaluatorTestCase extends AbstractRankingEvaluatorTestCase {

    @Override
    protected Evaluator<IntSet, IntList> getEvaluator(SparseMatrix featureMatrix) {
        return new NoveltyEvaluator(10, featureMatrix);
    }

    @Override
    protected float getMeasure() {
        return 9.993418F;
    }

}