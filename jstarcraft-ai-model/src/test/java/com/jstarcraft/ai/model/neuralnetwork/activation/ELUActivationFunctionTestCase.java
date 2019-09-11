package com.jstarcraft.ai.model.neuralnetwork.activation;

import org.nd4j.linalg.activations.IActivation;
import org.nd4j.linalg.activations.impl.ActivationELU;

import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.activation.ELUActivationFunction;

public class ELUActivationFunctionTestCase extends ActivationFunctionTestCase {

    @Override
    protected IActivation getOldFunction() {
        return new ActivationELU(0.5F);
    }

    @Override
    protected ActivationFunction getNewFunction() {
        return new ELUActivationFunction(0.5F);
    }

}
