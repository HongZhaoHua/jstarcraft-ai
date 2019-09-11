package com.jstarcraft.ai.model.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossMSLE;

import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.LossFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.MSLELossFunction;

public class MSLELossFunctionTestCase extends LossFunctionTestCase {

    @Override
    protected ILossFunction getOldFunction() {
        return new LossMSLE();
    }

    @Override
    protected LossFunction getNewFunction(ActivationFunction function) {
        return new MSLELossFunction();
    }

}
