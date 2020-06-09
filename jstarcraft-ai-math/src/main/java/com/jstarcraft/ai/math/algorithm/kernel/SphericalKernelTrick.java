package com.jstarcraft.ai.math.algorithm.kernel;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Spherical Kernel
 * 
 * <pre>
 * 可以用于替代{@link CircularKernelTrick}
 * </pre>
 * 
 * @author Birdy
 *
 */
public class SphericalKernelTrick extends RbfKernelTrick {

    private float sigma;

    public SphericalKernelTrick(boolean root, float sigma) {
        super(new NormDistance(1F, root));
        this.sigma = sigma;
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        coefficient = coefficient / sigma;
        return (float) (1F - (1.5F * coefficient) + (0.5F * FastMath.pow(coefficient, 3)));
    }

}
