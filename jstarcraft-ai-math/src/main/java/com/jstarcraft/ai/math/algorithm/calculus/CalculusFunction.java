package com.jstarcraft.ai.math.algorithm.calculus;

/**
 * 微积分函数
 * 
 * @author Birdy
 *
 */
public interface CalculusFunction {

    /**
     * 导函数
     * 
     * @param x
     * @return
     */
    float derivative(float x);

    /**
     * 原函数
     * 
     * @param x
     * @return
     */
    float primitive(float x);

}
