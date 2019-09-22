package com.jstarcraft.ai.math.algorithm.correlation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.SymmetryMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

public abstract class AbstractCorrelation implements Correlation {

    /**
     * 交集
     * 
     * @param leftVector
     * @param rightVector
     * @return
     */
    // TODO 准备使用Coefficient代替
    @Deprecated
    protected List<Float2FloatKeyValue> getIntersectionScores(MathVector leftVector, MathVector rightVector) {
        int leftCursor = 0, rightCursor = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
        List<Float2FloatKeyValue> scores = new ArrayList<>(FastMath.max(leftSize, rightSize));
        if (leftSize != 0 && rightSize != 0) {
            Iterator<VectorScalar> leftIterator = leftVector.iterator();
            Iterator<VectorScalar> rightIterator = rightVector.iterator();
            VectorScalar leftTerm = leftIterator.next();
            VectorScalar rightTerm = rightIterator.next();
            // 判断两个有序数组中是否存在相同的数字
            while (leftCursor < leftSize && rightCursor < rightSize) {
                if (leftTerm.getIndex() == rightTerm.getIndex()) {
                    scores.add(new Float2FloatKeyValue(leftTerm.getValue(), rightTerm.getValue()));
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
        return scores;
    }

    /**
     * 并集
     * 
     * @param leftVector
     * @param rightVector
     * @return
     */
    // TODO 准备使用Coefficient代替
    @Deprecated
    protected List<Float2FloatKeyValue> getUnionScores(MathVector leftVector, MathVector rightVector) {
        LinkedList<Float2FloatKeyValue> scores = new LinkedList<>();
        Iterator<VectorScalar> leftIterator = leftVector.iterator();
        Iterator<VectorScalar> rightIterator = rightVector.iterator();
        VectorScalar leftTerm = leftIterator.hasNext() ? leftIterator.next() : null;
        VectorScalar rightTerm = rightIterator.hasNext() ? rightIterator.next() : null;
        // 判断两个有序数组中是否存在相同的数字
        while (leftTerm != null || rightTerm != null) {
            if (leftTerm != null && rightTerm != null) {
                if (leftTerm.getIndex() == rightTerm.getIndex()) {
                    scores.add(new Float2FloatKeyValue(leftTerm.getValue(), rightTerm.getValue()));
                    leftTerm = leftIterator.hasNext() ? leftIterator.next() : null;
                    rightTerm = rightIterator.hasNext() ? rightIterator.next() : null;
                } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
                    scores.add(new Float2FloatKeyValue(Float.NaN, rightTerm.getValue()));
                    rightTerm = rightIterator.hasNext() ? rightIterator.next() : null;
                } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
                    scores.add(new Float2FloatKeyValue(leftTerm.getValue(), Float.NaN));
                    leftTerm = leftIterator.hasNext() ? leftIterator.next() : null;
                }
                continue;
            }
            if (leftTerm != null) {
                scores.add(new Float2FloatKeyValue(leftTerm.getValue(), Float.NaN));
                leftTerm = leftIterator.hasNext() ? leftIterator.next() : null;
                continue;
            }
            if (rightTerm != null) {
                scores.add(new Float2FloatKeyValue(Float.NaN, rightTerm.getValue()));
                rightTerm = rightIterator.hasNext() ? rightIterator.next() : null;
                continue;
            }
        }
        return scores;
    }

    @Override
    public SymmetryMatrix makeCorrelationMatrix(MathMatrix scoreMatrix, boolean transpose) {
        EnvironmentContext context = EnvironmentContext.getContext();
        Semaphore semaphore = new Semaphore(0);
        int count = transpose ? scoreMatrix.getColumnSize() : scoreMatrix.getRowSize();
        SymmetryMatrix similarityMatrix = new SymmetryMatrix(count);
        for (int leftIndex = 0; leftIndex < count; leftIndex++) {
            MathVector thisVector = transpose ? scoreMatrix.getColumnVector(leftIndex) : scoreMatrix.getRowVector(leftIndex);
            if (thisVector.getElementSize() == 0) {
                continue;
            }
            similarityMatrix.setValue(leftIndex, leftIndex, getIdentical());
            // user/item itself exclusive
            int permits = 0;
            for (int rightIndex = leftIndex + 1; rightIndex < count; rightIndex++) {
                MathVector thatVector = transpose ? scoreMatrix.getColumnVector(rightIndex) : scoreMatrix.getRowVector(rightIndex);
                if (thatVector.getElementSize() == 0) {
                    continue;
                }
                int leftCursor = leftIndex;
                int rightCursor = rightIndex;
                context.doAlgorithmByAny(leftIndex * rightIndex, () -> {
                    float similarity = getCoefficient(thisVector, thatVector);
                    if (!Double.isNaN(similarity)) {
                        similarityMatrix.setValue(leftCursor, rightCursor, similarity);
                    }
                    semaphore.release();
                });
                permits++;
            }
            try {
                semaphore.acquire(permits);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        return similarityMatrix;
    }

}
