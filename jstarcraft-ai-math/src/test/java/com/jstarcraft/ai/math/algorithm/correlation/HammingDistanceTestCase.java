package com.jstarcraft.ai.math.algorithm.correlation;

public class HammingDistanceTestCase extends AbstractDistanceTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new HammingDistance();
    }

}
