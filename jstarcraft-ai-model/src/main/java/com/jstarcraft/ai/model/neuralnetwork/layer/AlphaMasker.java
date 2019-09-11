package com.jstarcraft.ai.model.neuralnetwork.layer;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.neuralnetwork.activation.SELUActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.schedule.Schedule;
import com.jstarcraft.core.utility.RandomUtility;

/**
 * Alpha掩码器
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * 
 * 与{@link SELUActivationFunction}相关
 * </pre>
 * 
 * @author Birdy
 *
 */
public class AlphaMasker implements Masker {

    public static final float DEFAULT_ALPHA = 1.6732632423543772F;
    public static final float DEFAULT_LAMBDA = 1.0507009873554804F;

    private Schedule schedule;

    private Float value;
    private float prime;
    private float alpha;
    private float beta;

    public AlphaMasker(Schedule schedule) {
        this(schedule, DEFAULT_ALPHA, DEFAULT_LAMBDA);
    }

    public AlphaMasker(Schedule schedule, float alpha, float lambda) {
        this.schedule = schedule;
        this.prime = -lambda * alpha;
    }

    private float alpha(float probability) {
        return (float) (1F / Math.sqrt(probability + prime * prime * probability * (1F - probability)));
    }

    private float beta(float probability) {
        return -alpha(probability) * (1F - probability) * prime;
    }

    @Override
    public void mask(MathMatrix matrix, int iteration, int epoch) {
        // https://arxiv.org/pdf/1706.02515.pdf pg6
        // "...we propose “alpha dropout”, that randomly sets inputs to α'"
        // "The affine transformation a(xd + α'(1−d))+b allows to determine
        // parameters a and b such that mean and
        // variance are kept to their values"

        float probability = schedule.valueAt(iteration, epoch);
        if (probability != value) {
            alpha = alpha(probability);
            beta = beta(probability);
        }
        value = probability;

        // 参考Pytorch实现.
        // https://github.com/pytorch/pytorch/blob/master/torch/nn/functional.py
        matrix.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
            float value = scalar.getValue();
            value = RandomUtility.randomFloat(1F) < probability ? prime : value;
            scalar.setValue(value * alpha + beta);
        });
    }

}
