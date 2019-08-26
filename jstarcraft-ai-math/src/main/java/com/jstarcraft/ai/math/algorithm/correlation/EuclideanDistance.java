package com.jstarcraft.ai.math.algorithm.correlation;

import java.util.Iterator;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;

/**
 * Euclidean Distance相似度
 * 
 * <pre>
 * Euclidean Distance(欧几里得距离)
 * </pre>
 * 
 * @author Birdy
 *
 */
public class EuclideanDistance extends AbstractDistance {

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector, float scale) {
        // compute similarity
        float similarity = 0F;
        int leftCursor = 0, rightCursor = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
        if (leftSize != 0 && rightSize != 0) {
            Iterator<VectorScalar> leftIterator = leftVector.iterator();
            Iterator<VectorScalar> rightIterator = rightVector.iterator();
            VectorScalar leftTerm = leftIterator.next();
            VectorScalar rightTerm = rightIterator.next();
            // 判断两个有序数组中是否存在相同的数字
            float distance = 0F;
            while (leftCursor < leftSize && rightCursor < rightSize) {
                if (leftTerm.getIndex() == rightTerm.getIndex()) {
                    distance = leftTerm.getValue() - rightTerm.getValue();
                    leftTerm = leftIterator.next();
                    rightTerm = rightIterator.next();
                    leftCursor++;
                    rightCursor++;
                } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
                    distance = rightTerm.getValue();
                    rightTerm = rightIterator.next();
                    rightCursor++;
                } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
                    distance = leftTerm.getValue();
                    leftTerm = leftIterator.next();
                    leftCursor++;
                }
                similarity += distance * distance;
            }
        }
        if (similarity == 0F) {
            return similarity;
        } else {
            return (float) FastMath.sqrt(similarity);
        }
    }

}
