package com.jstarcraft.ai.math.algorithm.correlation;

public class NormDistanceTestCase extends AbstractDistanceTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new NormDistance(2F);
    }

}
