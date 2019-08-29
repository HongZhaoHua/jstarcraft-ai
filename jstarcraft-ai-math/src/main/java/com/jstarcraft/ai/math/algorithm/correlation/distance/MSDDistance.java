package com.jstarcraft.ai.math.algorithm.correlation.distance;

import java.util.List;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Mean Squared Difference距离
 * 
 * @author Birdy
 *
 */
public class MSDDistance extends AbstractDistance {

    private float getCoefficient(int count, List<Float2FloatKeyValue> scoreList) {
        if (count == 0) {
            return Float.NaN;
        }
        float similarity = 0F;
        for (Float2FloatKeyValue term : scoreList) {
            float distance = term.getKey() - term.getValue();
            similarity += distance * distance;
        }
        similarity = count / similarity;
        if (Float.isInfinite(similarity)) {
            similarity = 0F;
        }
        return similarity;
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
