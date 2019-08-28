package com.jstarcraft.ai.math.algorithm.correlation;

public class SpearmanRankCorrelationTestCase extends AbstractSimilarityTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new SpearmanRankCorrelation();
    }

}
