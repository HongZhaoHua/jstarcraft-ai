package com.jstarcraft.ai.model.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossFMeasure;

import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.FMeasureLossFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.LossFunction;

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
