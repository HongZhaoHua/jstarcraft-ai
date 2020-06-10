package com.jstarcraft.ai.math.algorithm.kernel;

public class SphericalKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new SphericalKernelTrick(false, 1F);
    }

}
