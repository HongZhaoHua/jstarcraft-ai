package com.jstarcraft.ai.math.algorithm.correlation.similarity;

import java.util.Iterator;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractSimilarity;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;

/**
 * Consistency index for a pair of subsets.
 * 
 * Based on definition 1 in L. Kuncheva 2007, A stability index for features
 * selection, Proceedings of the 25th IASTED international conference on
 * artificial intelligence and applications.
 * 
 * <code>
 * I(A,B)=r*n-k*k
 *        -------
 *        k*(n-k)
 * with:
 * n = original number of features, i.e. n=|X| with A &sub; X and B &sub; X
 * k = number of features in A and B, i.e. k=|A|=|B|
 * r = number of feature in common between A and B, i.e. r=|A &cap; B| 
 * </code>
 * 
 * @author Birdy
 * 
 */
public class ConsistencyIndexSimilarity extends AbstractSimilarity {

    /* original number of features. */
    private int n;

    public ConsistencyIndexSimilarity(int n) {
        this.n = n;
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector, float scale) {
        // compute similarity
        int intersection = 0;
        int leftCursor = 0, rightCursor = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();

        int k = Math.max(leftSize, rightSize);
        /* exceptional cases */
        if (k == 0 || k == n) {
            return 0;
        }

        if (leftSize != 0 && rightSize != 0) {
            Iterator<VectorScalar> leftIterator = leftVector.iterator();
            Iterator<VectorScalar> rightIterator = rightVector.iterator();
            VectorScalar leftTerm = leftIterator.next();
            VectorScalar rightTerm = rightIterator.next();
            // 判断两个有序数组中是否存在相同的数字
            while (leftCursor < leftSize && rightCursor < rightSize) {
                if (leftTerm.getIndex() == rightTerm.getIndex()) {
                    intersection++;
                    leftTerm = leftIterator.next();
                    rightTerm = rightIterator.next();
                    leftCursor++;
                    rightCursor++;
                } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
                    rightTerm = rightIterator.next();
                    rightCursor++;
                } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
                    leftTerm = leftIterator.next();
                    leftCursor++;
                }
            }
        }

        /* normal calculation */
        return (intersection * n - k * k) * 1F / (k * (n - k));
    }

}
