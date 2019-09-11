package com.jstarcraft.ai.model.neuralnetwork.parameter;

import com.jstarcraft.ai.math.algorithm.probability.Probability;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

public class DistributionParameterFactory implements ParameterFactory {

    private Probability<Number> probability;

    DistributionParameterFactory() {
    }

    public DistributionParameterFactory(Probability<Number> probability) {
        this.probability = probability;
    }

    @Override
    public void setValues(MathMatrix matrix) {
        // TODO Auto-generated method stub
    }

}
