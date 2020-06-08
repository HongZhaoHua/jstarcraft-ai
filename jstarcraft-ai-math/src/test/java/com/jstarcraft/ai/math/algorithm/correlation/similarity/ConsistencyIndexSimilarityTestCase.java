package com.jstarcraft.ai.math.algorithm.correlation.similarity;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractSimilarityTestCase;
import com.jstarcraft.ai.math.algorithm.correlation.MathCorrelation;

public class ConsistencyIndexSimilarityTestCase extends AbstractSimilarityTestCase {

    @Override
    protected MathCorrelation getCorrelation() {
        return new ConsistencyIndexSimilarity(1000);
    }

}
