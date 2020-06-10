package com.jstarcraft.ai.math.algorithm.kernel;

public class AnovaKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new AnovaKernelTrick(1F, 1F, 1F);
    }

}
