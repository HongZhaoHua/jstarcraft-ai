package com.jstarcraft.ai.math.algorithm.correlation.similarity;

import java.util.Iterator;
import java.util.List;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractSimilarity;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Kendall Rank Correlation相似度
 * 
 * @author Birdy
 *
 */
public class KRCCSimilarity extends AbstractSimilarity {

    private float getCoefficient(List<Float2FloatKeyValue> scores) {
        int count = scores.size();
        if (count < 2) {
            return 0F;
        }
        float sum = 0F;
        Iterator<Float2FloatKeyValue> iterator = scores.iterator();
        Float2FloatKeyValue previousTerm = iterator.next();
        Float2FloatKeyValue nextTerm = null;
        while (iterator.hasNext()) {
            nextTerm = iterator.next();
            float leftDelta = previousTerm.getKey() - nextTerm.getKey();
            float rightDelta = previousTerm.getValue() - nextTerm.getValue();
            if (leftDelta * rightDelta < 0F) {
                sum += 1D;
            }
            previousTerm = nextTerm;
        }
        return 1F - 4F * sum / (count * (count - 1));
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        List<Float2FloatKeyValue> scores = getIntersectionScores(leftVector, rightVector);
        float coefficient = getCoefficient(scores);
        return coefficient;
    }

}
