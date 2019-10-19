package com.jstarcraft.ai.math.algorithm.correlation.distance;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractCorrelation;
import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Levenstein Distance相似度
 * 
 * <pre>
 * Levenstein Distance(编辑距离)
 * </pre>
 * 
 * @author Birdy
 *
 */
public class LevenshteinDistance extends AbstractCorrelation implements MathDistance {

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        int leftSize = leftVector.getElementSize();
        int rightSize = rightVector.getElementSize();

        if (leftSize == 0) {
            return rightSize;
        } else if (rightSize == 0) {
            return leftSize;
        }

        int indexes[] = new int[leftSize + 1];
        int previous;
        int next;

        for (int index = 0; index <= leftSize; index++) {
            indexes[index] = index;
        }

        for (int rightIndex = 1; rightIndex <= rightSize; rightIndex++) {
            previous = indexes[0];
            float value = rightVector.getValue(rightIndex - 1);
            indexes[0] = rightIndex;

            for (int leftIndex = 1; leftIndex <= leftSize; leftIndex++) {
                next = indexes[leftIndex];
                int shift = leftVector.getValue(leftIndex - 1) == value ? 0 : 1;
                indexes[leftIndex] = Math.min(Math.min(indexes[leftIndex - 1] + 1, indexes[leftIndex] + 1), previous + shift);
                previous = next;
            }
        }

        return indexes[leftSize];
    }

}
