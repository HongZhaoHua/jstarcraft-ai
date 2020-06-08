package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * 二次有理核(Rational Quadratic Kernel)
 * 
 * @author Birdy
 *
 */
public class RationalQuadraticKernelTrick extends RbfKernelTrick {

    private float c;

    public RationalQuadraticKernelTrick(boolean root, float c) {
        super(new NormDistance(2F, root));
        this.c = c;
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return 1 - coefficient / (coefficient + c);
    }

}
