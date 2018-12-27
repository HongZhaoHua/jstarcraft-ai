package com.jstarcraft.ai.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossPoisson;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.loss.LossFunction;
import com.jstarcraft.ai.neuralnetwork.loss.PoissonLossFunction;

public class PoissonLossFunctionTestCase extends LossFunctionTestCase {

	@Override
	protected ILossFunction getOldFunction() {
		return new LossPoisson();
	}

	@Override
	protected LossFunction getNewFunction(ActivationFunction function) {
		return new PoissonLossFunction();
	}

}
