package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;
import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * 高斯核(Gaussian Kernel)
 * 
 * @author Birdy
 *
 */
public class GaussianKernelTrick extends RbfKernelTrick {

    private static final MathDistance distance = new NormDistance(2F, false);

    private float sigma;

    public GaussianKernelTrick(float sigma) {
        super(distance);
        this.sigma = 0.5F / (sigma * sigma);
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return (float) Math.exp(-coefficient * coefficient * sigma);
    }

}
