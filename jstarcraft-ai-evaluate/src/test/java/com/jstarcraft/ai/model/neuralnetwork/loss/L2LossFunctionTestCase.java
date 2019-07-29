package com.jstarcraft.ai.model.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossL2;

import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.L2LossFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.LossFunction;

public class L2LossFunctionTestCase extends LossFunctionTestCase {

	@Override
	protected ILossFunction getOldFunction() {
		return new LossL2();
	}

	@Override
	protected LossFunction getNewFunction(ActivationFunction function) {
		return new L2LossFunction();
	}

}
