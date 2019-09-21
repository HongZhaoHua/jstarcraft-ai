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

    private float getCoefficient(List<Float2FloatKeyValue> scores) {
        float coefficient = 0F;
        for (Float2FloatKeyValue term : scores) {
            if (term.getKey() != term.getValue()) {
                coefficient++;
            }
        }
        return coefficient;
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        List<Float2FloatKeyValue> scores = getUnionScores(leftVector, rightVector);
        float coefficient = getCoefficient(scores);
        return coefficient;
    }

}
