package com.jstarcraft.ai.math.algorithm.correlation;

public class ChebychevDistanceTestCase extends AbstractDistanceTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new ChebychevDistance();
    }

}
