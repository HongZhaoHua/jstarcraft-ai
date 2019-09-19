package com.jstarcraft.ai.math.algorithm.correlation.similarity;

import java.util.List;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractSimilarity;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Tanimoto相似度
 * 
 * @author Birdy
 *
 */
public class TanimotoSimilarity extends AbstractSimilarity {

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
        return power / (leftPower + rightPower - power);
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        List<Float2FloatKeyValue> scoreList = getScoreList(leftVector, rightVector);
        int count = scoreList.size();
        float coefficient = getCoefficient(count, scoreList);
        return coefficient;
    }

}
