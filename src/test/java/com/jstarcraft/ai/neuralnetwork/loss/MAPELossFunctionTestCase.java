package com.jstarcraft.ai.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossMAPE;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.loss.LossFunction;
import com.jstarcraft.ai.neuralnetwork.loss.MAPELossFunction;

public class MAPELossFunctionTestCase extends LossFunctionTestCase {

	@Override
	protected ILossFunction getOldFunction() {
		return new LossMAPE();
	}

	@Override
	protected LossFunction getNewFunction(ActivationFunction function) {
		return new MAPELossFunction();
	}

}
