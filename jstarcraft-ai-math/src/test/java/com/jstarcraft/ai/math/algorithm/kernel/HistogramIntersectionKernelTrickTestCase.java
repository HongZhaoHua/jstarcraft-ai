package com.jstarcraft.ai.math.algorithm.kernel;

public class HistogramIntersectionKernelTrickTestCase extends KernelTrickTestCase {

    @Override
    protected KernelTrick getKernelTrick() {
        return new HistogramIntersectionKernelTrick(1F, 1F);
    }

}
