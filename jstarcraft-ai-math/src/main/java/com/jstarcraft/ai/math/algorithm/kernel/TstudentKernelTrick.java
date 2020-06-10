package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * T-Student Kernel
 * 
 * @author Birdy
 *
 */
public class TstudentKernelTrick extends RbfKernelTrick {

    public TstudentKernelTrick(float power, boolean root) {
        super(new NormDistance(power, root));
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return 1F / (1F + coefficient);
    }

}
