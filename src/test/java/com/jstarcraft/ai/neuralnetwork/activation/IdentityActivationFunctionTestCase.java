package com.jstarcraft.ai.neuralnetwork.activation;

import org.nd4j.linalg.activations.IActivation;
import org.nd4j.linalg.activations.impl.ActivationIdentity;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.activation.IdentityActivationFunction;

public class IdentityActivationFunctionTestCase extends ActivationFunctionTestCase {

	@Override
	protected IActivation getOldFunction() {
		return new ActivationIdentity();
	}

	@Override
	protected ActivationFunction getNewFunction() {
		return new IdentityActivationFunction();
	}

}
