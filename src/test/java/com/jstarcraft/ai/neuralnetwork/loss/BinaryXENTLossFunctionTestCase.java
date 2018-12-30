package com.jstarcraft.ai.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossBinaryXENT;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.activation.SoftMaxActivationFunction;
import com.jstarcraft.ai.neuralnetwork.loss.BinaryXENTLossFunction;
import com.jstarcraft.ai.neuralnetwork.loss.LossFunction;

public class BinaryXENTLossFunctionTestCase extends LossFunctionTestCase {

	@Override
	protected ILossFunction getOldFunction() {
		return new LossBinaryXENT();
	}

	@Override
	protected LossFunction getNewFunction(ActivationFunction function) {
		return new BinaryXENTLossFunction(function instanceof SoftMaxActivationFunction);
	}

}
