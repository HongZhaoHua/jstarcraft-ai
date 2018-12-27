package com.jstarcraft.ai.neuralnetwork.activation;

import org.nd4j.linalg.activations.IActivation;
import org.nd4j.linalg.activations.impl.ActivationSigmoid;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.activation.SigmoidActivationFunction;

public class SigmoidActivationFunctionTestCase extends ActivationFunctionTestCase {

	@Override
	protected IActivation getOldFunction() {
		return new ActivationSigmoid();
	}

	@Override
	protected ActivationFunction getNewFunction() {
		return new SigmoidActivationFunction();
	}

}
