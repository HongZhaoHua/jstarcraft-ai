package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.KRCCSimilarity;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;

public class KRCCSimilarityTestCase extends AbstractSimilarityTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new KRCCSimilarity();
    }

}
