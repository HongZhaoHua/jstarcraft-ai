package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.JaccardIndexSimilarity;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;

public class JaccardIndexSimilarityTestCase extends AbstractSimilarityTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new JaccardIndexSimilarity();
    }

}
