package com.jstarcraft.ai.model.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossBinaryXENT;

import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.activation.SoftMaxActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.BinaryXENTLossFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.LossFunction;

public class BinaryXENTLossFunctionTestCase extends LossFunctionTestCase {

    @Override
    protected ILossFunction getOldFunction() {
        return new LossBinaryXENT();
    }

    @Override
    protected LossFunction getNewFunction(ActivationFunction function) {
        return new BinaryXENTLossFunction(function instanceof SoftMaxActivationFunction);
    }

}
