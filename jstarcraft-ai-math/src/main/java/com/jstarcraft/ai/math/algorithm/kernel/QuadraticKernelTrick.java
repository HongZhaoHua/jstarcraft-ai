package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Quadratic Kernel(二次有理核)
 * 
 * <pre>
 * 用于替代{@link MultiquadricKernel}
 * </pre>
 * 
 * @author Birdy
 *
 */
public class QuadraticKernelTrick extends RbfKernelTrick {

    private float c;

    public QuadraticKernelTrick(boolean root, float c) {
        super(new NormDistance(2F, root));
        this.c = c;
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return 1 - coefficient / (coefficient + c);
    }

}
