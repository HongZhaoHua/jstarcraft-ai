package com.jstarcraft.ai.math.algorithm.correlation;

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
public class LevensteinDistanceSimilarity extends AbstractSimilarity {

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector, float scale) {
        int leftSize = leftVector.getElementSize();
        int rightSize = rightVector.getElementSize();

        if (leftSize == 0 || rightSize == 0) {
            return leftSize == rightSize ? 1 : 0;
        }

        int previous[] = new int[leftSize + 1]; // 上一次计算的值
        int next[] = new int[leftSize + 1]; // 下一次计算的值
        int exchange[];

        for (int index = 0; index <= leftSize; index++) {
            previous[index] = index;
        }

        for (int rightIndex = 1; rightIndex <= rightSize; rightIndex++) {
            float value = rightVector.getValue(rightIndex - 1);
            next[0] = rightIndex;

            for (int leftIndex = 1; leftIndex <= leftSize; leftIndex++) {
                int shift = leftVector.getValue(leftIndex - 1) == value ? 0 : 1;
                next[leftIndex] = Math.min(Math.min(next[leftIndex - 1] + 1, previous[leftIndex] + 1), previous[leftIndex - 1] + shift);
            }

            exchange = previous;
            previous = next;
            next = exchange;
        }

        return 1F - (float) previous[leftSize] / Math.max(rightSize, leftSize);
    }

}
