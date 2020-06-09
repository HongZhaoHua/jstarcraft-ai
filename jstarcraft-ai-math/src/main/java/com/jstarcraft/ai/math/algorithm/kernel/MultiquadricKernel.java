package com.jstarcraft.ai.math.algorithm.kernel;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Multiquadric Kernel(多元二次核)
 * 
 * <pre>
 * 用于替代{@link QuadraticKernelTrick}
 * </pre>
 * 
 * @author Birdy
 *
 */
public class MultiquadricKernel extends RbfKernelTrick {

    private float power;

    private float c;

    public MultiquadricKernel(boolean root, float c, boolean inverse) {
        super(new NormDistance(2F, root));
        this.c = c;
        this.power = inverse ? -0.5F : 0.5F;
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return (float) FastMath.pow(coefficient + c * c, power);
    }

}
