package com.jstarcraft.ai.math.algorithm.correlation;

public abstract class AbstractDistanceTestCase extends AbstractCorrelationTestCase {

    @Override
    protected boolean checkCorrelation(float correlation) {
        return correlation >= 0F && correlation < Float.POSITIVE_INFINITY;
    }

    @Override
    protected float getIdentical() {
        return 0F;
    }

}
