package com.jstarcraft.ai.neuralnetwork.layer;

import org.apache.commons.math3.distribution.NormalDistribution;

import com.jstarcraft.ai.math.algorithm.distribution.ContinuousProbability;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.neuralnetwork.schedule.Schedule;

/**
 * GaussianNoiseOne掩码器
 * 
 * @author Birdy
 *
 */
public class GaussianNoiseOneMasker implements Masker {

	private Schedule schedule;

	protected GaussianNoiseOneMasker(Schedule schedule) {
		this.schedule = schedule;
	}

	@Override
	public void mask(MathMatrix matrix, int iteration, int epoch) {
		float current = schedule.valueAt(iteration, epoch);
		current = (float) Math.sqrt(current / (1F - current));

		ContinuousProbability probability = new ContinuousProbability(new NormalDistribution(1F, current));
		matrix.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			float value = scalar.getValue();
			scalar.setValue(value * probability.sample().floatValue());
		});
	}

}
