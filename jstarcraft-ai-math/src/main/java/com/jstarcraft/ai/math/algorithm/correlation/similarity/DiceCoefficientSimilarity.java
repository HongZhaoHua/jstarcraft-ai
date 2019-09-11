package com.jstarcraft.ai.math.algorithm.correlation.similarity;

import java.util.List;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractSimilarity;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Dice Coefficient相似度
 * 
 * @author Birdy
 *
 */
public class DiceCoefficientSimilarity extends AbstractSimilarity {

    private float getCoefficient(int count, List<Float2FloatKeyValue> scoreList) {
        if (count == 0) {
            return Float.NaN;
        }
        float power = 0F, leftPower = 0F, rightPower = 0F;
        for (Float2FloatKeyValue term : scoreList) {
            float leftScore = term.getKey();
            float rightScore = term.getValue();
            power += leftScore * rightScore;
            leftPower += leftScore * leftScore;
            rightPower += rightScore * rightScore;
        }
        return 2F * power / (leftPower + rightPower);
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector, float scale) {
        // compute similarity
        List<Float2FloatKeyValue> scoreList = getScoreList(leftVector, rightVector);
        int count = scoreList.size();
        float similarity = getCoefficient(count, scoreList);
        // shrink to account for vector size
        if (!Double.isNaN(similarity)) {
            if (scale > 0) {
                similarity *= count / (count + scale);
            }
        }
        return similarity;
    }

}
