package com.jstarcraft.ai.math.algorithm.probability;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.nd4j.linalg.api.rng.DefaultRandom;
import org.nd4j.linalg.api.rng.Random;
import org.nd4j.linalg.api.rng.distribution.Distribution;

public class BinomialProbabilityTestCase extends ProbabilityTestCase {

    @Override
    protected Distribution getOldFunction(int seed) {
        Random random = new DefaultRandom(seed);
        Distribution distribution = new org.nd4j.linalg.api.rng.distribution.impl.BinomialDistribution(random, 10, 0.5D);
        return distribution;
    }

    @Override
    protected MathProbability getNewFunction(int seed) {
        return new QualityProbability(MersenneTwister.class, seed, BinomialDistribution.class, 10, 0.5D);
    }

    @Override
    protected void assertSample(MathProbability newFuction, Distribution oldFunction) {
        Number newSample = newFuction.sample().doubleValue();
        Number oldSample = Math.ceil(oldFunction.sample());
        Assert.assertThat(newSample, CoreMatchers.equalTo(oldSample));
    }

}
