package com.jstarcraft.ai.neuralnetwork.activation;

import org.nd4j.linalg.activations.IActivation;
import org.nd4j.linalg.activations.impl.ActivationLReLU;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.activation.LReLUActivationFunction;

public class LReLUActivationFunctionTestCase extends ActivationFunctionTestCase {

	@Override
	protected IActivation getOldFunction() {
		return new ActivationLReLU();
	}

	@Override
	protected ActivationFunction getNewFunction() {
		return new LReLUActivationFunction();
	}

}
