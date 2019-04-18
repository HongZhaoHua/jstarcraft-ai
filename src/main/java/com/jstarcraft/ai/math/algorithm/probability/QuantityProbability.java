package com.jstarcraft.ai.math.algorithm.probability;

import org.apache.commons.math3.distribution.AbstractRealDistribution;

/**
 * 连续概率分布
 * 
 * @author Birdy
 *
 */
public class QuantityProbability implements Probability<Double> {

	private final AbstractRealDistribution distribution;

	public QuantityProbability(AbstractRealDistribution distribution) {
		this.distribution = distribution;
	}

	/**
	 * 概率密度函数(probability density function)是连续型随机变量在各个特定值的概率.
	 * 
	 * @param value
	 * @return
	 */
	public double density(Double value) {
		return distribution.density(value);
	}

	@Override
	public double cumulativeDistribution(Double value) {
		return distribution.cumulativeProbability(value);
	}

	@Override
	public double cumulativeDistribution(Double minimum, Double maximum) {
		return distribution.probability(minimum, maximum);
	}

	@Override
	public Double inverseDistribution(double probability) {
		return distribution.inverseCumulativeProbability(probability);
	}

	@Override
	public Double sample() {
		return distribution.sample();
	}

	@Override
	public Double getMaximum() {
		return distribution.getSupportUpperBound();
	}

	@Override
	public Double getMinimum() {
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
