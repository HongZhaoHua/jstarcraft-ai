package com.jstarcraft.ai.math.algorithm.correlation.distance;

import java.util.List;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * 切比雪夫距离
 * 
 * @author Birdy
 *
 */
public class ChebychevDistance extends AbstractDistance {

    private float getCoefficient(int count, List<Float2FloatKeyValue> scoreList) {
        float coefficient = 0F;
        for (Float2FloatKeyValue term : scoreList) {
            float distance = term.getKey() - term.getValue();
            coefficient = Math.max(coefficient, FastMath.abs(distance));
        }
        return coefficient;
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        List<Float2FloatKeyValue> scoreList = getScoreList(leftVector, rightVector);
        int count = scoreList.size();
        float coefficient = getCoefficient(count, scoreList);
        return coefficient;
    }

}
