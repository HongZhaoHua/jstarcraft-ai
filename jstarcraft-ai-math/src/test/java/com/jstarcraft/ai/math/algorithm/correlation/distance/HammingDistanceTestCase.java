package com.jstarcraft.ai.math.algorithm.correlation.distance;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistanceTestCase;
import com.jstarcraft.ai.math.algorithm.correlation.MathCorrelation;

public class HammingDistanceTestCase extends AbstractDistanceTestCase {

    @Override
    protected MathCorrelation getCorrelation() {
        return new HammingDistance();
    }

}
