package com.jstarcraft.ai.evaluate.rating;

import com.jstarcraft.ai.evaluate.AbstractRatingEvaluatorTestCase;
import com.jstarcraft.ai.evaluate.Evaluator;
import com.jstarcraft.ai.math.structure.matrix.SparseMatrix;

import it.unimi.dsi.fastutil.floats.FloatList;

public class MSEEvaluatorTestCase extends AbstractRatingEvaluatorTestCase {

    @Override
    protected Evaluator<FloatList, FloatList> getEvaluator(SparseMatrix featureMatrix) {
        return new MSEEvaluator(0F, 1F);
    }

    @Override
    protected float getMeasure() {
        return 0.041821852F;
    }

}