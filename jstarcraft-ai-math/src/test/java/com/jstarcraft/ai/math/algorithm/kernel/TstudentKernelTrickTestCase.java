package com.jstarcraft.ai.math.algorithm.kernel;

public class TstudentKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new TstudentKernelTrick(1F, false);
    }

}
