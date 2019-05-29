package com.jstarcraft.ai.math.algorithm.probability;

import org.apache.commons.math3.distribution.AbstractIntegerDistribution;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;

import com.jstarcraft.ai.modem.ModemCycle;
import com.jstarcraft.ai.modem.ModemDefinition;
import com.jstarcraft.core.common.reflection.ReflectionUtility;
import com.jstarcraft.core.utility.ArrayUtility;

/**
 * 连续概率分布
 * 
 * @author Birdy
 *
 */
@ModemDefinition(value = { "distributionClass", "distributionParameters", "randomClass", "randomSeed" })
public class QuantityProbability implements Probability<Double>, ModemCycle {

    private String distributionClass;

    private Object[] distributionParameters;

    private transient AbstractRealDistribution distribution;

    private String randomClass;

    private int randomSeed;

    private transient RandomGenerator random;

    QuantityProbability() {
    }

    public QuantityProbability(Class<? extends RandomGenerator> randomClazz, int randomSeed, Class<? extends AbstractRealDistribution> distributionClazz, Object... distributionParameters) {
        this.randomSeed = randomSeed;
        this.random = ReflectionUtility.getInstance(randomClazz, randomSeed);
        this.distributionParameters = distributionParameters;
        distributionParameters = ArrayUtility.insert(0, distributionParameters, random);
        this.distribution = ReflectionUtility.getInstance(distributionClazz, distributionParameters);
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

    @Override
    public void beforeSave() {
        distributionClass = distribution.getClass().getName();
        randomClass = random.getClass().getName();
    }

    @Override
    public void afterLoad() {
        try {
            random = (RandomGenerator) ReflectionUtility.getInstance(Class.forName(randomClass), randomSeed);
            Object[] parameters = ArrayUtility.insert(0, distributionParameters, random);
            distribution = (AbstractRealDistribution) ReflectionUtility.getInstance(Class.forName(distributionClass), parameters);
        } catch (Exception exception) {
        }
    }

}
