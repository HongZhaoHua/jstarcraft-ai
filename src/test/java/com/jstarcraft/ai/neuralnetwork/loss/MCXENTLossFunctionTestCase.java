package com.jstarcraft.ai.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossMCXENT;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.activation.SoftMaxActivationFunction;
import com.jstarcraft.ai.neuralnetwork.loss.LossFunction;
import com.jstarcraft.ai.neuralnetwork.loss.MCXENTLossFunction;

public class MCXENTLossFunctionTestCase extends LossFunctionTestCase {

	@Override
	protected ILossFunction getOldFunction() {
		return new LossMCXENT();
	}

	@Override
	protected LossFunction getNewFunction(ActivationFunction function) {
		return new MCXENTLossFunction(function instanceof SoftMaxActivationFunction);
	}

}
