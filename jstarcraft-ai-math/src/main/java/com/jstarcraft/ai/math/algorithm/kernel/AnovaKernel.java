package com.jstarcraft.ai.math.algorithm.kernel;

import java.util.List;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractCorrelation;
import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;

/**
 * ANOVA核(ANOVA Kernel)
 * 
 * @author Birdy
 *
 */
public class AnovaKernel extends RbfKernelTrick {

    private static final class AnovaDistance extends AbstractCorrelation implements MathDistance {

        private float power;

        public AnovaDistance(float power) {
            this.power = power;
        }

        private float getCoefficient(List<Float2FloatKeyValue> scores) {
            float distance = 0F;
            for (Float2FloatKeyValue term : scores) {
                distance += FastMath.pow(term.getKey(), power) - FastMath.pow(term.getValue(), power);
            }
            return distance * distance;
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

    private float sigma;

    private float d;

    public AnovaKernel(float k, float sigma, float d) {
        super(new AnovaDistance(k));
        this.sigma = sigma;
        this.d = d;
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return (float) Math.pow(Math.exp(-coefficient * sigma), d);
    }

}