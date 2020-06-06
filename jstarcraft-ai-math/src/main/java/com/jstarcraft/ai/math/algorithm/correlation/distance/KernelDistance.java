package com.jstarcraft.ai.math.algorithm.correlation.distance;

import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;
import com.jstarcraft.ai.math.algorithm.kernel.KernelTrick;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * 核距离
 * 
 * @author Birdy
 *
 */
public class KernelDistance implements MathDistance {

    private KernelTrick kernel;

    public KernelDistance(KernelTrick kernel) {
        this.kernel = kernel;
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        return kernel.calculate(leftVector, leftVector) + kernel.calculate(rightVector, rightVector) - kernel.calculate(leftVector, rightVector);
    }

}
