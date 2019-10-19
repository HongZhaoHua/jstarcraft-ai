package com.jstarcraft.ai.math.algorithm.probability;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.nd4j.linalg.api.rng.DefaultRandom;
import org.nd4j.linalg.api.rng.Random;
import org.nd4j.linalg.api.rng.distribution.Distribution;

public class UniformProbabilityTestCase extends ProbabilityTestCase {

    @Override
    protected Distribution getOldFunction(int seed) {
        Random random = new DefaultRandom(seed);
        Distribution distribution = new org.nd4j.linalg.api.rng.distribution.impl.UniformDistribution(random, 0.4D, 4D);
        return distribution;
    }

    @Override
    protected MathProbability getNewFunction(int seed) {
        return new QuantityProbability(MersenneTwister.class, seed, UniformRealDistribution.class, 0.4D, 4D);
    }

    @Override
    protected void assertSample(MathProbability newFuction, Distribution oldFunction) {
        Number newSample = newFuction.sample();
        Number oldSample = oldFunction.sample();
        Assert.assertThat(newSample, CoreMatchers.equalTo(oldSample));
    }

}
