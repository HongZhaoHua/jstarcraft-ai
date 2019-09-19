package com.jstarcraft.ai.math.algorithm.correlation.similarity;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractSimilarity;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Jaccard相似度
 * 
 * @author Birdy
 *
 */
public class JaccardIndexSimilarity extends AbstractSimilarity {

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        int intersection = getIntersectionSize(leftVector, rightVector);
        int leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
        float union = leftSize + rightSize - intersection;
        return (intersection) / union;
    }

}
