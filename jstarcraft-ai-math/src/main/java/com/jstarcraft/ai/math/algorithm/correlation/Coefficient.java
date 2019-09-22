package com.jstarcraft.ai.math.algorithm.correlation;

import com.jstarcraft.ai.math.structure.vector.VectorScalar;

public interface Coefficient {

    void calculateScore(VectorScalar leftScalar, VectorScalar rightScalar);
    
    float getScore();
    
}
