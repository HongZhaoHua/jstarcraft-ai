package com.jstarcraft.ai.math.algorithm.correlation;

public class ConsistencyIndexSimilarityTestCase extends AbstractSimilarityTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new ConsistencyIndexSimilarity(1000);
    }

}
