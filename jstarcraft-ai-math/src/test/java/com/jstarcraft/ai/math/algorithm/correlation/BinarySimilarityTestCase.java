package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.BinarySimilarity;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;

public class BinarySimilarityTestCase extends AbstractSimilarityTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new BinarySimilarity();
    }

}
