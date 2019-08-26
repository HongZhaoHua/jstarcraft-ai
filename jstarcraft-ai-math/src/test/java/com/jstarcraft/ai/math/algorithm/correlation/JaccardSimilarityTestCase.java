package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.JaccardSimilarity;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;

public class JaccardSimilarityTestCase extends AbstractSimilarityTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new JaccardSimilarity();
    }

}
