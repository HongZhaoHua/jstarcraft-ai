package com.jstarcraft.ai.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossHinge;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.loss.HingeLossFunction;
import com.jstarcraft.ai.neuralnetwork.loss.LossFunction;

public class HingeLossFunctionTestCase extends LossFunctionTestCase {

	@Override
	protected ILossFunction getOldFunction() {
		return new LossHinge();
	}

	@Override
	protected LossFunction getNewFunction(ActivationFunction function) {
		return new HingeLossFunction();
	}

}
