package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;
import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * 指数核(Exponential Kernel)
 * 
 * @author Birdy
 *
 */
public class ExponentialKernelTrick extends RbfKernelTrick {

    private static final MathDistance distance = new NormDistance(1F);

    private float sigma;

    public ExponentialKernelTrick(float sigma) {
        super(distance);
        this.sigma = 0.5F / (sigma * sigma);
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return (float) Math.exp(-coefficient * coefficient * sigma);
    }

}
