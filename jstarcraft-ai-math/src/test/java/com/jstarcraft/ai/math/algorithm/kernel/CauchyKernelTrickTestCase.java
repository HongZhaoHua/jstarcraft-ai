package com.jstarcraft.ai.math.algorithm.kernel;

public class CauchyKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new CauchyKernelTrick(false, 1F);
    }

}
