package com.jstarcraft.ai.model.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossMAPE;

import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.LossFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.MAPELossFunction;

public class MAPELossFunctionTestCase extends LossFunctionTestCase {

    @Override
    protected ILossFunction getOldFunction() {
        return new LossMAPE();
    }

    @Override
    protected LossFunction getNewFunction(ActivationFunction function) {
        return new MAPELossFunction();
    }

}
