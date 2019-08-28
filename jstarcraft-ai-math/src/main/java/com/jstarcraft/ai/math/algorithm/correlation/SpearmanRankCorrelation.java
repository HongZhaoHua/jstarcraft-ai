/**
 * %SVN.HEADER%
 */
package com.jstarcraft.ai.math.algorithm.correlation;

import java.util.List;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * 斯皮尔曼等级相关性
 * http://en.wikipedia.org/wiki/Spearman's_rank_correlation_coefficient
 * 
 * @author Birdy
 *
 */
public class SpearmanRankCorrelation extends AbstractSimilarity {

    private float getCoefficient(int count, List<Float2FloatKeyValue> scoreList) {
        float similarity = 0F;
        for (Float2FloatKeyValue term : scoreList) {
            float distance = term.getKey() - term.getValue();
            similarity += distance * distance;
        }
        return similarity;
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector, float scale) {
        // compute similarity
        List<Float2FloatKeyValue> scoreList = getScoreList(leftVector, rightVector);
        int count = scoreList.size();
        float numerator = getCoefficient(count, scoreList);
        int size = leftVector.getKnownSize() + leftVector.getUnknownSize();
        float denominator = size * (size * size - 1);
        return 1F - 6F * numerator / denominator;
    }

}
