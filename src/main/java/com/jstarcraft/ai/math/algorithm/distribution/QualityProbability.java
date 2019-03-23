package com.jstarcraft.ai.math.algorithm.distribution;

import org.apache.commons.math3.distribution.AbstractIntegerDistribution;

/**
 * 离散概率分布
 * 
 * @author Birdy
 *
 */
public class QualityProbability implements Probability<Integer> {

	private final AbstractIntegerDistribution distribution;

	public QualityProbability(AbstractIntegerDistribution distribution) {
		this.distribution = distribution;
	}

	/**
	 * 概率质量函数(probability mass function)是离散型随机变量在各个特定值的概率.
	 * 
	 * @param value
	 * @return
	 */
	public double mass(Integer value) {
		return distribution.probability(value);
	}

	@Override
	public double cumulativeDistribution(Integer value) {
		return distribution.cumulativeProbability(value);
	}

	@Override
	public double cumulativeDistribution(Integer minimum, Integer maximum) {
		return distribution.cumulativeProbability(minimum, maximum);
	}

	@Override
	public Integer inverseDistribution(double probability) {
		return distribution.inverseCumulativeProbability(probability);
	}

	@Override
	public Integer sample() {
		return distribution.sample();
	}

	@Override
	public Integer getMaximum() {
		return distribution.getSupportUpperBound();
	}

	@Override
	public Integer getMinimum() {
		return distribution.getSupportLowerBound();
	}

	@Override
	public double getMean() {
		return distribution.getNumericalMean();
	}

	@Override
	public double getVariance() {
		return distribution.getNumericalVariance();
	}

	@Override
	public void setSeed(long seed) {
		distribution.reseedRandomGenerator(seed);
	}

}
