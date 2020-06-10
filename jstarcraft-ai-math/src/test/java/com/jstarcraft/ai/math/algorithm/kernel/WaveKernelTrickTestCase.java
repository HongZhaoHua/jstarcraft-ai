package com.jstarcraft.ai.math.algorithm.kernel;

public class WaveKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new WaveKernelTrick(false);
    }

}
