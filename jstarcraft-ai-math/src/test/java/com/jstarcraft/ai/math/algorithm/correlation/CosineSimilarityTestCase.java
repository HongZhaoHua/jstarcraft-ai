package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.CosineSimilarity;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;

public class CosineSimilarityTestCase extends AbstractSimilarityTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new CosineSimilarity();
    }

}
