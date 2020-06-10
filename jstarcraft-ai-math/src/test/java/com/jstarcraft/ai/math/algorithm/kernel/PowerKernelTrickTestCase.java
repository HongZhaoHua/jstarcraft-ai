package com.jstarcraft.ai.math.algorithm.kernel;

public class PowerKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new PowerKernelTrick(1F, false);
    }

}
