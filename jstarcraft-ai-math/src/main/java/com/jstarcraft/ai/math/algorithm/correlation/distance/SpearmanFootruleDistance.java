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

    private float getCoefficient(List<Float2FloatKeyValue> scores) {
        float coefficient = 0F;
        for (Float2FloatKeyValue term : scores) {
            float distance = term.getKey() - term.getValue();
            coefficient += FastMath.abs(distance);
        }
        return coefficient;
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        List<Float2FloatKeyValue> scores = getIntersectionScores(leftVector, rightVector);
        float numerator = getCoefficient(scores);
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
