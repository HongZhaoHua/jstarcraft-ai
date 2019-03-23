package com.jstarcraft.ai.neuralnetwork.layer;

import org.apache.commons.math3.distribution.NormalDistribution;

import com.jstarcraft.ai.math.algorithm.distribution.QuantityProbability;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.neuralnetwork.schedule.Schedule;

/**
 * GaussianNoiseZero掩码器
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class GaussianNoiseZeroMasker implements Masker {

	private Schedule schedule;

	public GaussianNoiseZeroMasker(Schedule schedule) {
		this.schedule = schedule;
	}

	@Override
	public void mask(MathMatrix matrix, int iteration, int epoch) {
		float current = schedule.valueAt(iteration, epoch);

		QuantityProbability probability = new QuantityProbability(new NormalDistribution(0F, current));
		matrix.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			float value = scalar.getValue();
			scalar.setValue(value + probability.sample().floatValue());
		});
	}

}
