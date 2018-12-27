package com.jstarcraft.ai.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossFMeasure;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.loss.FMeasureLossFunction;
import com.jstarcraft.ai.neuralnetwork.loss.LossFunction;

public class FMeasureLossFunctionTestCase extends LossFunctionTestCase {

	@Override
	protected ILossFunction getOldFunction() {
		return new LossFMeasure();
	}

	@Override
	protected LossFunction getNewFunction(ActivationFunction function) {
		return new FMeasureLossFunction();
	}

}
