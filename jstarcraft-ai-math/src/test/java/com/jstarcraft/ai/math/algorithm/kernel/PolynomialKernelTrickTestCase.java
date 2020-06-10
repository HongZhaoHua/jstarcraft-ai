package com.jstarcraft.ai.math.algorithm.kernel;

public class PolynomialKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new PolynomialKernelTrick(1F, 1F, 1F);
    }

}
