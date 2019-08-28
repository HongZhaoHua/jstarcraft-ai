package com.jstarcraft.ai.math.algorithm.correlation;

public abstract class AbstractSimilarityTestCase extends AbstractCorrelationTestCase {

    @Override
    protected boolean checkCorrelation(float correlation) {
        return correlation < 1.00001F;
    }

    @Override
    protected float getIdentical() {
        return 1F;
    }

}