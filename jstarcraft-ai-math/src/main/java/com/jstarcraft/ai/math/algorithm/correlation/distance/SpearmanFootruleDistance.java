package com.jstarcraft.ai.math.algorithm.correlation.distance;

import java.util.List;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * 简捷距离
 * 
 * @author Birdy
 *
 */
public class SpearmanFootruleDistance extends AbstractDistance {

    private float getCoefficient(int count, List<Float2FloatKeyValue> scoreList) {
        float similarity = 0F;
        for (Float2FloatKeyValue term : scoreList) {
            float distance = term.getKey() - term.getValue();
            similarity += FastMath.abs(distance);
        }
        return similarity;
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        List<Float2FloatKeyValue> scoreList = getScoreList(leftVector, rightVector);
        int count = scoreList.size();
        float numerator = getCoefficient(count, scoreList);
        int size = leftVector.getKnownSize() + leftVector.getUnknownSize();
        float denominator;
        if (size % 2 == 0) {
            denominator = (size * size) / 2F;
        } else {
            denominator = ((size + 1F) * (size - 1F)) / 2F;
        }
        return numerator / denominator;
    }

}
