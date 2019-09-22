package com.jstarcraft.ai.evaluate.ranking;

import java.util.concurrent.Future;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentFactory;
import com.jstarcraft.ai.evaluate.AbstractRankingEvaluatorTestCase;
import com.jstarcraft.ai.evaluate.Evaluator;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;
import com.jstarcraft.ai.math.algorithm.correlation.similarity.CosineSimilarity;
import com.jstarcraft.ai.math.structure.matrix.SparseMatrix;
import com.jstarcraft.ai.math.structure.matrix.SymmetryMatrix;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;

public class DiversityEvaluatorTestCase extends AbstractRankingEvaluatorTestCase {

    @Override
    protected Evaluator<IntSet, IntList> getEvaluator(SparseMatrix featureMatrix) {
        Correlation correlation = new CosineSimilarity();
        EnvironmentContext context = EnvironmentFactory.getContext();
        Future<SymmetryMatrix> task = context.doTask(() -> {
            SymmetryMatrix similarityMatrix = correlation.calculateCoefficients(featureMatrix, true);
            return similarityMatrix;
        });
        try {
            return new DiversityEvaluator(10, task.get());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    protected float getMeasure() {
        return 0.74845463F;
    }

}