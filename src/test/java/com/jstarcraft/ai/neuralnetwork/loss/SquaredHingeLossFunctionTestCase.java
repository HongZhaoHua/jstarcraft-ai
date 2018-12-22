package com.jstarcraft.ai.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossSquaredHinge;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.loss.LossFunction;
import com.jstarcraft.ai.neuralnetwork.loss.SquaredHingeLossFunction;

public class SquaredHingeLossFunctionTestCase extends LossFunctionTestCase {

	@Override
	protected ILossFunction getOldFunction() {
		return new LossSquaredHinge();
	}

	@Override
	protected LossFunction getNewFunction(ActivationFunction function) {
		return new SquaredHingeLossFunction();
	}

}
