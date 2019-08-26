package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.EuclideanDistance;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;

public class EuclideanDistanceTestCase extends AbstractDistanceTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new EuclideanDistance();
    }

}
