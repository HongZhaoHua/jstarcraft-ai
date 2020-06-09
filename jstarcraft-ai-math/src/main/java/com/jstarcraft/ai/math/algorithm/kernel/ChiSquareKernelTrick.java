package com.jstarcraft.ai.math.algorithm.kernel;

import java.util.List;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractCorrelation;
import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Chi-Square Kernel(卡方核)
 * 
 * @author Birdy
 *
 */
public class ChiSquareKernelTrick extends RbfKernelTrick {

    private static final class ChiSquareDistance extends AbstractCorrelation implements MathDistance {

        private float getCoefficient(List<Float2FloatKeyValue> scores) {
            float coefficient = 0F;
            for (Float2FloatKeyValue term : scores) {
                float leftScalar = term.getKey();
                float rightScalar = term.getValue();
                float subtract = leftScalar - rightScalar;
                float add = leftScalar + rightScalar;
                float distance = subtract * subtract * 2F / add ;
                coefficient += distance;
            }
            return coefficient;
        }

        @Override
        public float getCoefficient(MathVector leftVector, MathVector rightVector) {
            List<Float2FloatKeyValue> scores = getIntersectionScores(leftVector, rightVector);
            int intersection = scores.size();
            if (intersection == 0) {
                return Float.POSITIVE_INFINITY;
            }
            int union = leftVector.getElementSize() + rightVector.getElementSize() - intersection;
            float coefficient = getCoefficient(scores);
            coefficient *= union;
            coefficient /= intersection;
            return coefficient;
        }

    }

    public ChiSquareKernelTrick() {
        super(new ChiSquareDistance());
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return coefficient;
    }

}
