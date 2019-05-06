package com.jstarcraft.ai.math.algorithm.probability;

import org.apache.commons.math3.distribution.AbstractIntegerDistribution;
import org.apache.commons.math3.random.RandomGenerator;

import com.jstarcraft.ai.modem.ModemCycle;
import com.jstarcraft.ai.modem.ModemDefinition;
import com.jstarcraft.core.utility.ArrayUtility;
import com.jstarcraft.core.utility.ReflectionUtility;

/**
 * 离散概率分布
 * 
 * @author Birdy
 *
 */
@ModemDefinition(value = { "distributionClass", "distributionParameters", "randomClass", "randomSeed" })
public class QualityProbability implements Probability<Integer>, ModemCycle {

    private String distributionClass;

    private Object[] distributionParameters;

    private transient AbstractIntegerDistribution distribution;

    private String randomClass;

    private long randomSeed;

    private transient RandomGenerator random;

    QualityProbability() {
    }

    public QualityProbability(Class<? extends RandomGenerator> randomClazz, long randomSeed, Class<? extends AbstractIntegerDistribution> distributionClazz, Object... distributionParameters) {
        this.randomSeed = randomSeed;
        this.random = ReflectionUtility.getInstance(randomClazz, randomSeed);
        this.distributionParameters = distributionParameters;
        distributionParameters = ArrayUtility.insert(0, distributionParameters, random);
        this.distribution = ReflectionUtility.getInstance(distributionClazz, distributionParameters);
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
        randomSeed = seed;
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
            distribution = (AbstractIntegerDistribution) ReflectionUtility.getInstance(Class.forName(distributionClass), parameters);
        } catch (Exception exception) {
        }
    }

}
