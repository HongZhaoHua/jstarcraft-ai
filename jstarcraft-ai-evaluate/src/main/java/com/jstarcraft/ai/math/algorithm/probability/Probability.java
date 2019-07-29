package com.jstarcraft.ai.math.algorithm.probability;

/**
 * 概率分布
 * 
 * @author Birdy
 *
 */
public interface Probability<T extends Number> {

    public final static long DEFAULT_SEED = 0L;

    /**
     * 累积分布函数 (cumulative distribution function)
     * 
     * <pre>
     * 不管是什么类型(连续/离散/其他)的随机变量,都可以定义它的累积分布函数.
     * </pre>
     * 
     * @param value
     * @return
     */
    double cumulativeDistribution(T value);

    /**
     * 累积分布函数 (cumulative distribution function)
     * 
     * <pre>
     * 不管是什么类型(连续/离散/其他)的随机变量,都可以定义它的累积分布函数.
     * </pre>
     * 
     * @param minimum
     * @param maximum
     * @return
     */
    double cumulativeDistribution(T minimum, T maximum);

    T inverseDistribution(double probability);

    T sample();

    T getMaximum();

    T getMinimum();

    double getMean();

    double getVariance();

    void setSeed(long seed);

}
