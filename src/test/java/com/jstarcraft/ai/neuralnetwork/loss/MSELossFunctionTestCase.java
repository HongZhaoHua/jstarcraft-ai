package com.jstarcraft.ai.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossMSE;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.loss.LossFunction;
import com.jstarcraft.ai.neuralnetwork.loss.MSELossFunction;

public class MSELossFunctionTestCase extends LossFunctionTestCase {

	@Override
	protected ILossFunction getOldFunction() {
		return new LossMSE();
	}

	@Override
	protected LossFunction getNewFunction(ActivationFunction function) {
		return new MSELossFunction();
	}

}
