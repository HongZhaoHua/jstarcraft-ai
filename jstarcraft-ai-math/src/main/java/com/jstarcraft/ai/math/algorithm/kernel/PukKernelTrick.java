package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * PUK Kernel
 * 
 * @author Birdy
 *
 */
public class PukKernelTrick extends RbfKernelTrick {

    private float sigma;

    private float omega;

    private float cnst;

    public PukKernelTrick(boolean root, float sigma, float omega) {
        super(new NormDistance(2F, root));
        this.sigma = sigma;
        this.omega = omega;
        this.cnst = (float) Math.sqrt(Math.pow(2F, 1F / omega) - 1F);
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        coefficient = 2F * coefficient * cnst / sigma;
        return 1F / (float) Math.pow(1F + coefficient * coefficient, omega);
    }

}
