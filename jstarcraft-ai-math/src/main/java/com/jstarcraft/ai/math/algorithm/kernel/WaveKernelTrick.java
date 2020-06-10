package com.jstarcraft.ai.math.algorithm.kernel;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.correlation.distance.ChebychevDistance;
import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Wave Kernel
 * 
 * <pre>
 * 用于语音处理
 * </pre>
 * 
 * @author Birdy
 *
 */
public class WaveKernelTrick extends RbfKernelTrick {

    private ChebychevDistance chebychev;

    public WaveKernelTrick(boolean root) {
        super(new NormDistance(1F, root));
        this.chebychev = new ChebychevDistance();
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float theta = chebychev.getCoefficient(leftVector, rightVector) + 1F;
        float coefficient = distance.getCoefficient(leftVector, rightVector) + 1F;
        return (float) (theta / coefficient * FastMath.sin(coefficient / theta));
    }

}
