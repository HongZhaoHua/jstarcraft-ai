package com.jstarcraft.ai.math.algorithm.kernel;

import java.util.Iterator;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;

/**
 * Chi-Square Kernel(卡方核)
 * 
 * @author Birdy
 *
 */
public class ChiSquareKernelTrick implements KernelTrick {

    private float calculate(float leftScalar, float rightScalar) {
        float subtract = leftScalar - rightScalar;
        float add = leftScalar + rightScalar;
        float coefficient = (subtract * subtract) / (add / 2F);
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
        return 1F - coefficient;
    }

}
