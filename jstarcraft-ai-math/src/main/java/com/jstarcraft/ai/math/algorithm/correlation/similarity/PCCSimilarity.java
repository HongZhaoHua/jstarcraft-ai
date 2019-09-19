package com.jstarcraft.ai.math.algorithm.correlation.similarity;

import java.util.List;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractSimilarity;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Pearson Correlation Coefficient相似度
 * 
 * @author Birdy
 *
 */
public class PCCSimilarity extends AbstractSimilarity {

    private float getCoefficient(int count, List<Float2FloatKeyValue> scoreList) {
        if (count < 2) {
            return Float.NaN;
        }
        float leftMean = 0F;
        float rightMean = 0F;
        for (Float2FloatKeyValue term : scoreList) {
            leftMean += term.getKey();
            rightMean += term.getValue();
        }
        leftMean /= count;
        rightMean /= count;
        float sum = 0F, leftPower = 0F, rightPower = 0F;
        for (Float2FloatKeyValue term : scoreList) {
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
        List<Float2FloatKeyValue> scoreList = getScoreList(leftVector, rightVector);
        int count = scoreList.size();
        float coefficient = getCoefficient(count, scoreList);
        return coefficient;
    }

}
