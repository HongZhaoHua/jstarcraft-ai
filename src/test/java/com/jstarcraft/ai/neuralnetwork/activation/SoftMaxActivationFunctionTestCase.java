package com.jstarcraft.ai.neuralnetwork.activation;

import org.nd4j.linalg.activations.IActivation;
import org.nd4j.linalg.activations.impl.ActivationSoftmax;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.activation.SoftMaxActivationFunction;

public class SoftMaxActivationFunctionTestCase extends ActivationFunctionTestCase {

	@Override
	protected IActivation getOldFunction() {
		return new ActivationSoftmax();
	}

	@Override
	protected ActivationFunction getNewFunction() {
		return new SoftMaxActivationFunction();
	}

}
