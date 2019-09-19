package com.jstarcraft.ai.math.algorithm.correlation.distance;

import java.util.List;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Manhattan Distance曼哈顿距离
 * 
 * @author Birdy
 *
 */
public class ManhattanDistance extends AbstractDistance {

    private float getCoefficient(List<Float2FloatKeyValue> scores) {
        float coefficient = 0F;
        for (Float2FloatKeyValue term : scores) {
            float distance = term.getKey() - term.getValue();
            coefficient += FastMath.abs(distance);
        }
        if (coefficient == 0F) {
            return coefficient;
        } else {
            return (float) FastMath.sqrt(coefficient);
        }
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        List<Float2FloatKeyValue> scores = getIntersectionScores(leftVector, rightVector);
        int intersection = scores.size();
        if (intersection == 0) {
            return Float.POSITIVE_INFINITY;
        }
        int union = leftVector.getElementSize() + rightVector.getElementSize() - intersection;
        float coefficient = getCoefficient(scores);
        coefficient *= union;
        coefficient /= intersection;
        return coefficient;
    }

}
