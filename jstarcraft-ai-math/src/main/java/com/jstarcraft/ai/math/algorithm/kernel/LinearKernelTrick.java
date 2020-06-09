package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.structure.DefaultScalar;
import com.jstarcraft.ai.math.structure.MathScalar;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Linear Kernel(线性核)
 * 
 * @author Birdy
 *
 */
public class LinearKernelTrick implements KernelTrick {

    private float c;

    public LinearKernelTrick(float c) {
        this.c = c;
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        MathScalar scalar = DefaultScalar.getInstance();
        return scalar.dotProduct(leftVector, rightVector).getValue() + c;
    }

}
