package com.jstarcraft.ai.math.algorithm.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.SynchronizedRandomGenerator;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.nd4j.linalg.api.rng.DefaultRandom;
import org.nd4j.linalg.api.rng.Random;
import org.nd4j.linalg.api.rng.distribution.Distribution;

public class UniformProbabilityTestCase extends ProbabilityTestCase {

	@Override
	protected Distribution getOldFunction(long seed) {
		Random random = new DefaultRandom(seed);
		Distribution distribution = new org.nd4j.linalg.api.rng.distribution.impl.UniformDistribution(random, 0.4D, 4D);
		return distribution;
	}

	@Override
	protected Probability getNewFunction(long seed) {
		RandomGenerator random = new SynchronizedRandomGenerator(new MersenneTwister(seed));
		UniformRealDistribution distribution = new UniformRealDistribution(random, 0.4D, 4D);
		return new ContinuousProbability(distribution);
	}

	@Override
	protected void assertSample(Probability newFuction, Distribution oldFunction) {
		Number newSample = newFuction.sample();
		Number oldSample = oldFunction.sample();
		Assert.assertThat(newSample, CoreMatchers.equalTo(oldSample));
	}

}
