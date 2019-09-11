package com.jstarcraft.ai.model.neuralnetwork.activation;

import org.nd4j.linalg.activations.IActivation;
import org.nd4j.linalg.activations.impl.ActivationHardSigmoid;

import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.activation.HardSigmoidActivationFunction;

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
