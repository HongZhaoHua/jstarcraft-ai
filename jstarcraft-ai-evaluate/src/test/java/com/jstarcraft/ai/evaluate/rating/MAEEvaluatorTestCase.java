package com.jstarcraft.ai.evaluate.rating;

import com.jstarcraft.ai.evaluate.AbstractRatingEvaluatorTestCase;
import com.jstarcraft.ai.evaluate.Evaluator;
import com.jstarcraft.ai.math.structure.matrix.SparseMatrix;

import it.unimi.dsi.fastutil.floats.FloatList;

public class MAEEvaluatorTestCase extends AbstractRatingEvaluatorTestCase {

    @Override
    protected Evaluator<FloatList, FloatList> getEvaluator(SparseMatrix featureMatrix) {
        return new MAEEvaluator(0F, 1F);
    }

    @Override
    protected float getMeasure() {
        return 0.12530328F;
    }

}