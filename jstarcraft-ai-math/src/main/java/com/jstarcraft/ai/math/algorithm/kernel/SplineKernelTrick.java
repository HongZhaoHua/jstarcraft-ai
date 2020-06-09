package com.jstarcraft.ai.math.algorithm.kernel;

import java.util.List;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractCorrelation;
import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * Spline Kernel
 * 
 * @author Birdy
 *
 */
public class SplineKernelTrick extends RbfKernelTrick {

    private static final class SplineDistance extends AbstractCorrelation implements MathDistance {

        private float getCoefficient(List<Float2FloatKeyValue> scores) {
            float coefficient = 0F;
            for (Float2FloatKeyValue term : scores) {
                float leftScalar = term.getKey();
                float rightScalar = term.getValue();
                float multiply = leftScalar * rightScalar;
                float minimum = FastMath.min(leftScalar, rightScalar);
                float add = leftScalar + rightScalar;
                float distance = 1F;
                distance = distance + multiply;
                distance = distance + multiply * minimum;
                distance = distance - add / 2F * minimum * minimum;
                distance = distance + minimum * minimum * minimum / 3;
                coefficient *= distance;
            }
            return coefficient;
        }

        @Override
        public float getCoefficient(MathVector leftVector, MathVector rightVector) {
            List<Float2FloatKeyValue> scores = getIntersectionScores(leftVector, rightVector);
            if (scores.size() == 0) {
                return Float.POSITIVE_INFINITY;
            }
            float coefficient = getCoefficient(scores);
            return coefficient;
        }

    }

    public SplineKernelTrick() {
        super(new SplineDistance());
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return coefficient;
    }

}
