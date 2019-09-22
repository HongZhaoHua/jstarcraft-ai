package com.jstarcraft.ai.math.algorithm.correlation.similarity;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractSimilarityTestCase;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;

public class CPCSimilarityTestCase extends AbstractSimilarityTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new CPCSimilarity(0.5F);
    }

}
