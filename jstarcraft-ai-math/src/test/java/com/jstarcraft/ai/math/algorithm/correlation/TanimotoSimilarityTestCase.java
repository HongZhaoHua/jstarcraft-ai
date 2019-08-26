package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.Correlation;
import com.jstarcraft.ai.math.algorithm.correlation.TanimotoSimilarity;

public class TanimotoSimilarityTestCase extends AbstractSimilarityTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new TanimotoSimilarity();
    }

}
