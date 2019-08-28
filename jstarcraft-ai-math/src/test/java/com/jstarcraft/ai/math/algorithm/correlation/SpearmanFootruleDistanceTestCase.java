package com.jstarcraft.ai.math.algorithm.correlation;

public class SpearmanFootruleDistanceTestCase extends AbstractDistanceTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new SpearmanFootruleDistance();
    }

}
