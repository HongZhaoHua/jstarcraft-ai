package com.jstarcraft.ai.neuralnetwork.activation;

import org.nd4j.linalg.activations.IActivation;
import org.nd4j.linalg.activations.impl.ActivationHardSigmoid;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.activation.HardSigmoidActivationFunction;

public class HardSigmoidActivationFunctionTestCase extends ActivationFunctionTestCase {

	@Override
	protected IActivation getOldFunction() {
		return new ActivationHardSigmoid();
	}

	@Override
	protected ActivationFunction getNewFunction() {
		return new HardSigmoidActivationFunction();
	}

}
