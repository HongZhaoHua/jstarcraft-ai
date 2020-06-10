package com.jstarcraft.ai.math.algorithm.kernel;

public class LinearKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new LinearKernelTrick(1F);
    }

}
