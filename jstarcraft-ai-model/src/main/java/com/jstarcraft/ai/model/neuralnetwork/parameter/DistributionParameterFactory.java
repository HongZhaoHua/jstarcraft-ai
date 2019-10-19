package com.jstarcraft.ai.model.neuralnetwork.parameter;

import com.jstarcraft.ai.math.algorithm.probability.MathProbability;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

public class DistributionParameterFactory implements ParameterFactory {

    private MathProbability<Number> probability;

    DistributionParameterFactory() {
    }

    public DistributionParameterFactory(MathProbability<Number> probability) {
        this.probability = probability;
    }

    @Override
    public void setValues(MathMatrix matrix) {
        // TODO Auto-generated method stub
    }

}
