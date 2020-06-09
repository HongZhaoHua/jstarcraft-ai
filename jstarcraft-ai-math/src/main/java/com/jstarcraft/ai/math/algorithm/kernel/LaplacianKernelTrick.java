package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Laplacian Kernel(拉普拉斯核)
 * 
 * @author Birdy
 *
 */
public class LaplacianKernelTrick extends RbfKernelTrick {

    private float sigma;

    public LaplacianKernelTrick(boolean root, float sigma) {
        super(new NormDistance(1F, root));
        this.sigma = sigma;
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return (float) Math.exp(-coefficient * coefficient * sigma);
    }

}
