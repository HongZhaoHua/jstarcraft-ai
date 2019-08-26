package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.CPCSimilarity;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;

public class CPCSimilarityTestCase extends AbstractSimilarityTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new CPCSimilarity();
    }

}
