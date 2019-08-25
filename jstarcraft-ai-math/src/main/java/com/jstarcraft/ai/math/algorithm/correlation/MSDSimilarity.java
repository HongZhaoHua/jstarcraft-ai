package com.jstarcraft.ai.math.algorithm.correlation;

import java.util.List;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Mean Squared Difference相似度
 * 
 * @author Birdy
 *
 */
public class MSDSimilarity extends AbstractSimilarity {

    private float getSimilarity(int count, List<Float2FloatKeyValue> scoreList) {
        if (count == 0) {
            return Float.NaN;
        }
        float sum = 0F;
        for (Float2FloatKeyValue term : scoreList) {
            sum += Math.pow(term.getKey() - term.getValue(), 2);
        }
        float similarity = count / sum;
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
        float similarity = getSimilarity(count, scoreList);
        // shrink to account for vector size
        if (!Double.isNaN(similarity)) {
            if (scale > 0) {
                similarity *= count / (count + scale);
            }
        }
        return similarity;
    }

    @Override
    public float getIdentical() {
        return 0F;
    }

}
