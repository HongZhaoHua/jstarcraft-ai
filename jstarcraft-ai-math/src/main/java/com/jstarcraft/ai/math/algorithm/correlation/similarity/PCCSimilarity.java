package com.jstarcraft.ai.math.algorithm.correlation.similarity;

import java.util.List;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractCorrelation;
import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;
import com.jstarcraft.ai.math.algorithm.correlation.MathSimilarity;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Pearson Correlation Coefficient相似度
 * 
 * @author Birdy
 *
 */
public class PCCSimilarity extends AbstractCorrelation implements MathSimilarity {

    private float getCoefficient(List<Float2FloatKeyValue> scores) {
        int count = scores.size();
        float leftMean = 0F;
        float rightMean = 0F;
        for (Float2FloatKeyValue term : scores) {
            leftMean += term.getKey();
            rightMean += term.getValue();
        }
        leftMean /= count;
        rightMean /= count;
        float sum = 0F, leftPower = 0F, rightPower = 0F;
        for (Float2FloatKeyValue term : scores) {
            float leftDelta = term.getKey() - leftMean;
            float rightDelta = term.getValue() - rightMean;
            sum += leftDelta * rightDelta;
            leftPower += leftDelta * leftDelta;
            rightPower += rightDelta * rightDelta;
        }
        return (float) (sum / (Math.sqrt(leftPower) * Math.sqrt(rightPower)));
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        List<Float2FloatKeyValue> scores = getIntersectionScores(leftVector, rightVector);
        int intersection = scores.size();
        if (intersection == 0) {
            return 0F;
        }
        int union = leftVector.getElementSize() + rightVector.getElementSize() - intersection;
        float coefficient = getCoefficient(scores);
        coefficient *= intersection;
        coefficient /= union;
        return coefficient;
    }

}
