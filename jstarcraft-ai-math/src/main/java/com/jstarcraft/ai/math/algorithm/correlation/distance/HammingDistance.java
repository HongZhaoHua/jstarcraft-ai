package com.jstarcraft.ai.math.algorithm.correlation.distance;

import java.util.List;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * 汉明距离
 * 
 * @author Birdy
 *
 */
public class HammingDistance extends AbstractDistance {

    private float getCoefficient(int count, List<Float2FloatKeyValue> scoreList) {
        float similarity = 0F;
        for (Float2FloatKeyValue term : scoreList) {
            float distance = term.getKey() - term.getValue();
            if (distance != 0F) {
                similarity++;
            }
        }
        return similarity;
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        List<Float2FloatKeyValue> scoreList = getScoreList(leftVector, rightVector);
        int count = scoreList.size();
        float coefficient = getCoefficient(count, scoreList);
        return coefficient;
    }

}
