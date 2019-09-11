package com.jstarcraft.ai.model.neuralnetwork.activation;

import org.nd4j.linalg.activations.IActivation;
import org.nd4j.linalg.activations.impl.ActivationReLU;

import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.activation.ReLUActivationFunction;

public class ReLUActivationFunctionTestCase extends ActivationFunctionTestCase {

    @Override
    protected IActivation getOldFunction() {
        return new ActivationReLU();
    }

    @Override
    protected ActivationFunction getNewFunction() {
        return new ReLUActivationFunction();
    }

}
