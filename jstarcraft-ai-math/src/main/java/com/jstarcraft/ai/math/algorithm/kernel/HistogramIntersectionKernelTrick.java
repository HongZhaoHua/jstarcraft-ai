package com.jstarcraft.ai.math.algorithm.kernel;

import java.util.Iterator;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;

/**
 * Histogram Intersection Kernel
 * 
 * <pre>
 * 用于图像处理
 * </pre>
 * 
 * @author Birdy
 *
 */
public class HistogramIntersectionKernelTrick implements KernelTrick {

    private float a;

    private float b;

    public HistogramIntersectionKernelTrick(float a, float b) {
        this.a = a;
        this.b = b;
    }

    private float calculate(float leftScalar, float rightScalar) {
        float coefficient = (float) FastMath.min(FastMath.pow(FastMath.abs(leftScalar), a), FastMath.pow(FastMath.abs(rightScalar), b));
        return coefficient;
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        int leftCursor = 0, rightCursor = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
        float coefficient = 0F;
        int count = 0;
        if (leftSize != 0 && rightSize != 0) {
            Iterator<VectorScalar> leftIterator = leftVector.iterator();
            Iterator<VectorScalar> rightIterator = rightVector.iterator();
            VectorScalar leftTerm = leftIterator.next();
            VectorScalar rightTerm = rightIterator.next();
            // 判断两个有序数组中是否存在相同的数字
            while (leftCursor < leftSize && rightCursor < rightSize) {
                if (leftTerm.getIndex() == rightTerm.getIndex()) {
                    coefficient += calculate(leftTerm.getValue(), rightTerm.getValue());
                    count++;
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
        return coefficient / count;
    }

}
