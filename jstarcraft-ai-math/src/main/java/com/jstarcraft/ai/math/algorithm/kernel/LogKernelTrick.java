package com.jstarcraft.ai.math.algorithm.kernel;

import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.algorithm.correlation.distance.NormDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * Log Kernel
 * 
 * <pre>
 * 用于图像处理
 * </pre>
 * 
 * @author Birdy
 *
 */
public class LogKernelTrick extends RbfKernelTrick {

    public LogKernelTrick(float power, boolean root) {
        super(new NormDistance(power, root));
    }

    @Override
    public float calculate(MathVector leftVector, MathVector rightVector) {
        float coefficient = distance.getCoefficient(leftVector, rightVector);
        return (float) -FastMath.log(coefficient + 1F);
    }

}
