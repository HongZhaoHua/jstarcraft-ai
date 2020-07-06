package com.jstarcraft.ai.model.neuralnetwork.activation;

import org.nd4j.linalg.activations.IActivation;
import org.nd4j.linalg.activations.impl.ActivationCube;

public class PowerActivationFunctionTestCase extends ActivationFunctionTestCase {

    @Override
    protected IActivation getOldFunction() {
        return new ActivationCube();
    }

    @Override
    protected ActivationFunction getNewFunction() {
        return new PowerActivationFunction(3);
    }

}
