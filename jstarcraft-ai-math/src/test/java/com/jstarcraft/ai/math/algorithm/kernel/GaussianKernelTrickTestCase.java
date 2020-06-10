package com.jstarcraft.ai.math.algorithm.kernel;

public class GaussianKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new GaussianKernelTrick(false, 1F);
    }

}
