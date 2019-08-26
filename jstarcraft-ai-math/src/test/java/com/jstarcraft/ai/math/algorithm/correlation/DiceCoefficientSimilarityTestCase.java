package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.algorithm.correlation.DiceCoefficientSimilarity;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;

public class DiceCoefficientSimilarityTestCase extends AbstractSimilarityTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new DiceCoefficientSimilarity();
    }

}
