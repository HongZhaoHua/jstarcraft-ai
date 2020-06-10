package com.jstarcraft.ai.math.algorithm.kernel;

public class LogKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new LogKernelTrick(1F, false);
    }

}
