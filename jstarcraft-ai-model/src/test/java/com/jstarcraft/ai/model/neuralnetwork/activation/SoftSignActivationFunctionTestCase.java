package com.jstarcraft.ai.model.neuralnetwork.activation;

import org.nd4j.linalg.activations.IActivation;
import org.nd4j.linalg.activations.impl.ActivationSoftSign;

import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.activation.SoftSignActivationFunction;

public class SoftSignActivationFunctionTestCase extends ActivationFunctionTestCase {

    @Override
    protected IActivation getOldFunction() {
        return new ActivationSoftSign();
    }

    @Override
    protected ActivationFunction getNewFunction() {
        return new SoftSignActivationFunction();
    }

}
