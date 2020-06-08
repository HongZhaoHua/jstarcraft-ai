package com.jstarcraft.ai.math.algorithm.kernel;

import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * 核技巧
 * 
 * @author Birdy
 *
 */
public interface KernelTrick {

    /**
     * 计算核
     * 
     * @param leftVector
     * @param rightVector
     * @return
     */
    float calculate(MathVector leftVector, MathVector rightVector);

    /**
     * 是否归一化
     * 
     * @return
     */
//    boolean normalized();

}
