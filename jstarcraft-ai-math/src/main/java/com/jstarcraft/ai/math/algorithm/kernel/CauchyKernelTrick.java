package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Cauchy Kernel(柯西核)
 * 
 * <pre>
 * 用于维度很高的数据
 * </pre>
 * 
 * @author Birdy
 *
 */
public class CauchyKernelTrick extends RbfKernelTrick {

    private float sigma;

    public CauchyKernelTrick(boolean root, float sigma) {
        super(new NormDistance(2F, root));
        this.sigma = sigma;
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return 1F / (1F + coefficient / sigma);
    }

}
