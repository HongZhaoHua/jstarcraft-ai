package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Gaussian Kernel(高斯核)
 * 
 * @author Birdy
 *
 */
public class GaussianKernelTrick extends RbfKernelTrick {

    private float sigma;

    public GaussianKernelTrick(boolean root, float sigma) {
        super(new NormDistance(2F, root));
        this.sigma = 0.5F / (sigma * sigma);
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return (float) Math.exp(-coefficient * coefficient * sigma);
    }

}
