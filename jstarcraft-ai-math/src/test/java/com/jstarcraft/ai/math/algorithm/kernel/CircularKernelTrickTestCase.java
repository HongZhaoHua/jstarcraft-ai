package com.jstarcraft.ai.math.algorithm.kernel;

public class CircularKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new CircularKernelTrick(false, 1F);
    }

}
