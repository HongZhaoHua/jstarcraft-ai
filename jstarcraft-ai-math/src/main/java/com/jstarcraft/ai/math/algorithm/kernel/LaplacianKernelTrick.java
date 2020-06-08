package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * 拉普拉斯核(Laplacian Kernel)
 * 
 * @author Birdy
 *
 */
public class LaplacianKernelTrick extends RbfKernelTrick {

    private static final NormDistance distance = new NormDistance(1F, false);

    private float sigma;

    public LaplacianKernelTrick(float sigma) {
        super(distance);
        this.sigma = sigma;
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return (float) Math.exp(-coefficient * coefficient * sigma);
    }

}
