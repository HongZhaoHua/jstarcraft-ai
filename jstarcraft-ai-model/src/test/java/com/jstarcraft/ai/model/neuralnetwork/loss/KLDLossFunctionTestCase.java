package com.jstarcraft.ai.model.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossKLD;

import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.KLDLossFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.LossFunction;

public class KLDLossFunctionTestCase extends LossFunctionTestCase {

    @Override
    protected ILossFunction getOldFunction() {
        return new LossKLD();
    }

    @Override
    protected LossFunction getNewFunction(ActivationFunction function) {
        return new KLDLossFunction();
    }

}
