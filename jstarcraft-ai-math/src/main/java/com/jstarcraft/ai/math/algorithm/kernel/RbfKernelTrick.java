package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;

/**
 * Radial Basis Function(径向基核)
 * 
 * @author Birdy
 *
 */
public abstract class RbfKernelTrick implements KernelTrick {

    protected MathDistance distance;

    protected RbfKernelTrick(MathDistance distance) {
        this.distance = distance;
    }

}
