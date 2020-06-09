package com.jstarcraft.ai.math.algorithm.kernel;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractCorrelation;
import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Spline Kernel
 * 
 * @author Birdy
 *
 */
public class SplineKernelTrick implements KernelTrick {

    private float calculate(float leftScalar, float rightScalar) {
        float multiply = leftScalar * rightScalar;
        float minimum = FastMath.min(leftScalar, rightScalar);
        float add = leftScalar + rightScalar;
        float coefficient = 1F;
        coefficient = coefficient + multiply;
        coefficient = coefficient + multiply * minimum;
        coefficient = coefficient - add / 2F * minimum * minimum;
        coefficient = coefficient + minimum * minimum * minimum / 3;
        return coefficient;
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        int leftCursor = 0, rightCursor = 0, leftSize = leftVector.getElementSize(), rightSize = rightVector.getElementSize();
        float coefficient = 1F;
        if (leftSize != 0 && rightSize != 0) {
            Iterator<VectorScalar> leftIterator = leftVector.iterator();
            Iterator<VectorScalar> rightIterator = rightVector.iterator();
            VectorScalar leftTerm = leftIterator.next();
            VectorScalar rightTerm = rightIterator.next();
            // 判断两个有序数组中是否存在相同的数字
            while (leftCursor < leftSize && rightCursor < rightSize) {
                if (leftTerm.getIndex() == rightTerm.getIndex()) {
                    coefficient *= calculate(leftTerm.getValue(), rightTerm.getValue());
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
        return coefficient;
    }

}
