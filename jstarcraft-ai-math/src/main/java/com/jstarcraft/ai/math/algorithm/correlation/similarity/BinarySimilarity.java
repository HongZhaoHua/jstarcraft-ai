package com.jstarcraft.ai.math.algorithm.correlation.similarity;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractSimilarity;
import com.jstarcraft.ai.math.structure.DefaultScalar;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Binary Cosine相似度
 * 
 * @author Birdy
 *
 */
public class BinarySimilarity extends AbstractSimilarity {

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        DefaultScalar scalar = DefaultScalar.getInstance();
        float numerator = scalar.dotProduct(leftVector, rightVector).getValue();
        float denominator = 0F;
        denominator += Math.sqrt(scalar.dotProduct(leftVector, leftVector).getValue());
        denominator *= Math.sqrt(scalar.dotProduct(rightVector, rightVector).getValue());
        return numerator / denominator;
    }

}