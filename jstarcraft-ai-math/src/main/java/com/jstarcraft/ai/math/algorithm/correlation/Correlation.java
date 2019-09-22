package com.jstarcraft.ai.math.algorithm.correlation;

import java.util.concurrent.Semaphore;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.SymmetryMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * 相关度
 * 
 * @author Birdy
 *
 */
public interface Correlation {

    /**
     * 根据分数矩阵计算相关度
     * 
     * @param scoreMatrix
     * @param transpose
     * @param monitor
     */
    default void calculateCoefficients(MathMatrix scoreMatrix, boolean transpose, CorrelationMonitor monitor) {
        EnvironmentContext context = EnvironmentContext.getContext();
        Semaphore semaphore = new Semaphore(0);
        int count = transpose ? scoreMatrix.getColumnSize() : scoreMatrix.getRowSize();
        for (int leftIndex = 0; leftIndex < count; leftIndex++) {
            MathVector thisVector = transpose ? scoreMatrix.getColumnVector(leftIndex) : scoreMatrix.getRowVector(leftIndex);
            if (thisVector.getElementSize() == 0) {
                continue;
            }
            monitor.notifyCoefficientCalculated(leftIndex, leftIndex, getIdentical());
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
                    float coefficient = getCoefficient(thisVector, thatVector);
                    if (!Double.isNaN(coefficient)) {
                        monitor.notifyCoefficientCalculated(leftCursor, rightCursor, coefficient);
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
    }

    /**
     * 获取两个向量的相关系数
     * 
     * @param leftVector
     * @param rightVector
     * @param scale
     * @return
     */
    float getCoefficient(MathVector leftVector, MathVector rightVector);

    /**
     * 获取恒等系数
     * 
     * @return
     */
    float getIdentical();

}
