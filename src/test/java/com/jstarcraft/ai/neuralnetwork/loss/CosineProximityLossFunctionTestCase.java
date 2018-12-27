package com.jstarcraft.ai.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossCosineProximity;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.loss.CosineProximityLossFunction;
import com.jstarcraft.ai.neuralnetwork.loss.LossFunction;

public class CosineProximityLossFunctionTestCase extends LossFunctionTestCase {

	@Override
	protected ILossFunction getOldFunction() {
		return new LossCosineProximity();
	}

	@Override
	protected LossFunction getNewFunction(ActivationFunction function) {
		return new CosineProximityLossFunction();
	}

}
