package com.jstarcraft.ai.math.algorithm.kernel;

public class MultiquadricKernelTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new MultiquadricKernel(false, 1F, true);
    }

}
