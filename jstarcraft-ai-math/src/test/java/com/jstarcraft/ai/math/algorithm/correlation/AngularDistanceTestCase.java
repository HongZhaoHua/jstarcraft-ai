package com.jstarcraft.ai.math.algorithm.correlation;

public class AngularDistanceTestCase extends AbstractDistanceTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new AngularDistance();
    }

}
