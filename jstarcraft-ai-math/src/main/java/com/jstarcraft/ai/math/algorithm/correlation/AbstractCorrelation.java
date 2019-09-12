package com.jstarcraft.ai.math.algorithm.correlation;

import java.util.List;
import java.util.concurrent.Semaphore;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.SymmetryMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

public abstract class AbstractCorrelation implements Correlation {

    protected abstract List<Float2FloatKeyValue> getScoreList(MathVector leftVector, MathVector rightVector);

    @Override
    public SymmetryMatrix makeCorrelationMatrix(MathMatrix scoreMatrix, boolean transpose, float scale) {
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
                    float similarity = getCoefficient(thisVector, thatVector, scale);
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
