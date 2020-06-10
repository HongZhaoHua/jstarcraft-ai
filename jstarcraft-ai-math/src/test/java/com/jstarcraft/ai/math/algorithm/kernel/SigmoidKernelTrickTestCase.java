package com.jstarcraft.ai.math.algorithm.kernel;

public class SigmoidKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new SigmoidKernelTrick(0.001F, 0.001F);
    }

}
