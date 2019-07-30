package com.jstarcraft.ai.model.neuralnetwork.parameter;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

public class XavierUniformParameterFactory implements ParameterFactory {

    @Override
    public void setValues(MathMatrix matrix) {
        double value = Math.sqrt(6F) / Math.sqrt(matrix.getRowSize() + matrix.getColumnSize());
        INDArray ndArray = Nd4j.rand(new int[] { matrix.getRowSize(), matrix.getColumnSize() }, Nd4j.getDistributions().createUniform(-value, value));
        matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            scalar.setValue(ndArray.getFloat(scalar.getRow(), scalar.getColumn()));
        });
    }

}