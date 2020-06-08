package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;

/**
 * 径向基核(Radial Basis Function)
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
