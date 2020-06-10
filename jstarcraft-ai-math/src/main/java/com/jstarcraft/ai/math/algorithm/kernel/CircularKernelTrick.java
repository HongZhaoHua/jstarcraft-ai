package com.jstarcraft.ai.math.algorithm.kernel;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Circular Kernel
 * 
 * @author Birdy
 *
 */
public class CircularKernelTrick extends RbfKernelTrick {

    private float pi;

    private float sigma;

    private NormDistance euclidean;

    public CircularKernelTrick(boolean root, float sigma) {
        super(new NormDistance(1F, root));
        this.pi = (float) (2F / FastMath.PI);
        this.sigma = sigma;
        this.euclidean = new NormDistance(2F, root);
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        if (coefficient > sigma) {
            return 0F;
        } else {
            coefficient = coefficient / sigma;
            return (float) ((pi * FastMath.acos(-coefficient)) - (pi * coefficient * FastMath.sqrt(1F - euclidean.getCoefficient(leftVector, rightVector) / sigma)));
        }
    }

}
