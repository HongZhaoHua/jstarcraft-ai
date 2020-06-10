package com.jstarcraft.ai.math.algorithm.kernel;

public class ExponentialKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new ExponentialKernelTrick(false, 1F);
    }

}
