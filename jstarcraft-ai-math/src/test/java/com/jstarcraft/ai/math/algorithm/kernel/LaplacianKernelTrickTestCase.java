package com.jstarcraft.ai.math.algorithm.kernel;

public class LaplacianKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new LaplacianKernelTrick(false, 1F);
    }

}
