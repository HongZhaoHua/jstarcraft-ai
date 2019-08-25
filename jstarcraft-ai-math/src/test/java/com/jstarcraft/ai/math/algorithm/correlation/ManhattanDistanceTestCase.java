package com.jstarcraft.ai.math.algorithm.correlation;

public class ManhattanDistanceTestCase extends AbstractSimilarityTestCase {

    @Override
    protected boolean checkCorrelation(float correlation) {
        return correlation >= 0F && correlation < Float.POSITIVE_INFINITY;
    }

    @Override
    protected float getIdentical() {
        return 0F;
    }

    @Override
    protected Correlation getCorrelation() {
        return new ManhattanDistance();
    }

}
