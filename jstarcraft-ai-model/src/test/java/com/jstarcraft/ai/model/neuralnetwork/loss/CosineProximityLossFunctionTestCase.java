package com.jstarcraft.ai.model.neuralnetwork.loss;

import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossCosineProximity;

import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.CosineProximityLossFunction;
import com.jstarcraft.ai.model.neuralnetwork.loss.LossFunction;

public class CosineProximityLossFunctionTestCase extends LossFunctionTestCase {

    @Override
    protected ILossFunction getOldFunction() {
        return new LossCosineProximity();
    }

    @Override
    protected LossFunction getNewFunction(ActivationFunction function) {
        return new CosineProximityLossFunction();
    }

}
