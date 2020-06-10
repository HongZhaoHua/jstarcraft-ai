package com.jstarcraft.ai.math.algorithm.kernel;

public class PukKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new PukKernelTrick(false, 1F, 1F);
    }

}
