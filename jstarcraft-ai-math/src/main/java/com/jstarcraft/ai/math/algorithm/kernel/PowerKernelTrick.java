package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Power Kernel/Triangular Kernel(三角核)
 * 
 * @author Birdy
 *
 */
public class PowerKernelTrick extends RbfKernelTrick {

    public PowerKernelTrick(float power, boolean root) {
        super(new NormDistance(power, root));
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return -coefficient;
    }

}
