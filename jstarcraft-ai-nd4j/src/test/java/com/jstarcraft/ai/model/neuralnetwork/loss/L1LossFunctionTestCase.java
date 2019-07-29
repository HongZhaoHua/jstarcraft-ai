package com.jstarcraft.ai.model.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossL1;

import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.L1LossFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.LossFunction;

public class L1LossFunctionTestCase extends LossFunctionTestCase {

	@Override
	protected ILossFunction getOldFunction() {
		return new LossL1();
	}

	@Override
	protected LossFunction getNewFunction(ActivationFunction function) {
		return new L1LossFunction();
	}

}
