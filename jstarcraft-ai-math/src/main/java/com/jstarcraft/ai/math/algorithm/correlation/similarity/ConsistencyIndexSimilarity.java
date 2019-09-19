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
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        int k = Math.max(leftVector.getElementSize(), rightVector.getElementSize());
        /* exceptional cases */
        if (k == 0 || k == n) {
            return 0;
        }
        /* normal calculation */
        return (getIntersectionSize(leftVector, rightVector) * n - k * k) * 1F / (k * (n - k));
    }

}
