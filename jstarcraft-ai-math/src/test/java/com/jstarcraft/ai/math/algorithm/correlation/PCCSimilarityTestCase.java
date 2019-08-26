package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.PCCSimilarity;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;

public class PCCSimilarityTestCase extends AbstractSimilarityTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new PCCSimilarity();
    }

}
