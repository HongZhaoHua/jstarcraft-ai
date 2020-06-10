package com.jstarcraft.ai.math.algorithm.kernel;

public class QuadraticKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new QuadraticKernelTrick(false, 1F);
    }

}
