package com.jstarcraft.ai.math.algorithm.correlation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.SymmetryMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

public abstract class AbstractSimilarity implements Similarity {

    protected final List<Float2FloatKeyValue> getScoreList(MathVector leftVector, MathVector rightVector) {
        LinkedList<Float2FloatKeyValue> scoreList = new LinkedList<>();
        int leftIndex = 0, rightIndex = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
        if (leftSize != 0 && rightSize != 0) {
            Iterator<VectorScalar> leftIterator = leftVector.iterator();
            Iterator<VectorScalar> rightIterator = rightVector.iterator();
            VectorScalar leftTerm = leftIterator.next();
            VectorScalar rightTerm = rightIterator.next();
            // 判断两个有序数组中是否存在相同的数字
            while (leftIndex < leftSize && rightIndex < rightSize) {
                if (leftTerm.getIndex() == rightTerm.getIndex()) {
                    scoreList.add(new Float2FloatKeyValue(leftTerm.getValue(), rightTerm.getValue()));
                    leftTerm = leftIterator.next();
                    rightTerm = rightIterator.next();
                    leftIndex++;
                    rightIndex++;
                } else if (leftTerm.getIndex() > rightTerm.getIndex()) {
                    rightTerm = rightIterator.next();
                    rightIndex++;
                } else if (leftTerm.getIndex() < rightTerm.getIndex()) {
                    leftTerm = leftIterator.next();
                    leftIndex++;
                }
            }
        }
        return scoreList;
    }

    @Override
    public SymmetryMatrix makeSimilarityMatrix(MathMatrix scoreMatrix, boolean transpose, float scale) {
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
                    float similarity = getCorrelation(thisVector, thatVector, scale);
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
