package com.jstarcraft.ai.model.pg.supervised.classify;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.attribute.QualityAttribute;
import com.jstarcraft.ai.model.Updateable;
import com.jstarcraft.ai.model.estimate.DiscreteEstimator;
import com.jstarcraft.ai.model.estimate.Estimator;
import com.jstarcraft.ai.model.supervised.Practicer;
import com.jstarcraft.ai.model.supervised.Predictor;

/**
 * 朴素贝叶斯分类器
 * 
 * @author Birdy
 *
 */
public class NaiveBayesianClassifier implements Practicer, Predictor, Updateable<DataInstance> {

    /*** The precision parameter used for numeric attributes */
    protected static final double DEFAULT_NUM_PRECISION = 0.01;
    
    /** 分类属性 */
    private QualityAttribute attribute;

    /** 先验概率 */
    private Estimator<Integer> priorDistribution;

    /** 后验概率 */
    private Estimator<Integer>[][] posteriorQualityDistributions;

    private Estimator<Float>[][] posteriorQuantityDistributions;

    public NaiveBayesianClassifier(QualityAttribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public void practice(DataModule module, DataModule... contexts) {
        // 每个属性的概率估计器
        int size = attribute.getSize();
        priorDistribution = new DiscreteEstimator(size, true);
        posteriorQualityDistributions = new Estimator[module.getQualityOrder()][size];
        posteriorQuantityDistributions = new Estimator[module.getQuantityOrder()][size];
        for (int mark = 0; mark < size; mark++) {
            for (int dimension = 0, order = module.getQualityOrder(); dimension < order; dimension++) {
                posteriorQualityDistributions[dimension][mark] = new DiscreteEstimator(size, true);
            }
            for (int dimension = 0, order = module.getQuantityOrder(); dimension < order; dimension++) {
                // TODO 处理连续属性
            }
        }

        // 计算条件概率
        for (DataInstance instance : module) {
            update(instance);
        }
    }

    protected float[] predict(DataInstance instance) {
        int size = attribute.getSize();
        float[] probabilities = new float[size];
        for (int mark = 0; mark < size; mark++) {
            probabilities[mark] = priorDistribution.getProbability(mark);
        }

        float weight = instance.getWeight();
        for (int dimension = 0, order = instance.getQualityOrder(); dimension < order; dimension++) {
            int data = instance.getQualityFeature(dimension);
            if (data != DataInstance.defaultInteger) {
                float probability, distribution = 0F;
                for (int mark = 0; mark < size; mark++) {
                    probability = (float) Math.max(1E-75, Math.pow(posteriorQualityDistributions[dimension][mark].getProbability(data), weight));
                    probabilities[mark] *= probability;
                    if (probabilities[mark] > distribution) {
                        distribution = probabilities[mark];
                    }
                    if (Float.isNaN(probabilities[mark])) {
                        throw new RuntimeException();
                    }
                }
                if ((distribution > 0) && (distribution < 1E-75)) { // Danger of probability underflow
                    for (int mark = 0; mark < size; mark++) {
                        probabilities[mark] *= 1E75;
                    }
                }
            }
        }

        for (int dimension = 0, order = instance.getQuantityOrder(); dimension < order; dimension++) {
            float data = instance.getQuantityFeature(dimension);
            if (data != DataInstance.defaultFloat) {
                float probability, distribution = 0F;
                for (int mark = 0; mark < size; mark++) {
                    probability = (float) Math.max(1E-75, Math.pow(posteriorQuantityDistributions[dimension][mark].getProbability(data), weight));
                    probabilities[mark] *= probability;
                    if (probabilities[mark] > distribution) {
                        distribution = probabilities[mark];
                    }
                    if (Float.isNaN(probabilities[mark])) {
                        throw new RuntimeException();
                    }
                }
                if ((distribution > 0) && (distribution < 1E-75)) { // Danger of probability underflow
                    for (int mark = 0; mark < size; mark++) {
                        probabilities[mark] *= 1E75;
                    }
                }
            }
        }

        return probabilities;
    }

    @Override
    public void predict(DataInstance instance, DataInstance... contexts) {
        float[] probabilities = predict(instance);
        int mark = -1;
        float probability = Float.NEGATIVE_INFINITY;
        for (int index = 0, size = probabilities.length; index < size; index++) {
            if (probabilities[index] > probability) {
                probability = probabilities[index];
                mark = index;
            }
        }
        instance.setQualityMark(mark);
    }

    @Override
    public void update(DataInstance instance) {
        // 更新条件概率
        int mark = instance.getQualityMark();
        float weight = instance.getWeight();
        if (mark > DataInstance.defaultInteger) {
            for (int dimension = 0, order = instance.getQualityOrder(); dimension < order; dimension++) {
                int data = instance.getQualityFeature(dimension);
                if (data != DataInstance.defaultInteger) {
                    posteriorQualityDistributions[dimension][mark].updateProbability(data, weight);
                }
            }
            for (int dimension = 0, order = instance.getQuantityOrder(); dimension < order; dimension++) {
                float data = instance.getQuantityFeature(dimension);
                if (data != DataInstance.defaultFloat) {
                    posteriorQuantityDistributions[dimension][mark].updateProbability(data, weight);
                }
            }
            priorDistribution.updateProbability(instance.getQualityMark(), instance.getWeight());
        }
    }

}
