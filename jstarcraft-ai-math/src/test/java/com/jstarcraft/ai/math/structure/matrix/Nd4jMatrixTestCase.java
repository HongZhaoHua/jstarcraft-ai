package com.jstarcraft.ai.math.structure.matrix;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.core.utility.RandomUtility;

public class Nd4jMatrixTestCase extends MatrixTestCase {

    @Override
    protected Nd4jMatrix getRandomMatrix(int dimension) {
        INDArray array = Nd4j.zeros(dimension, dimension, 'c');
        Nd4jMatrix matrix = new Nd4jMatrix(array);
        matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            scalar.setValue(RandomUtility.randomInteger(dimension));
        });
        return matrix;
    }

    @Override
    protected Nd4jMatrix getZeroMatrix(int dimension) {
        INDArray array = Nd4j.zeros(dimension, dimension, 'f');
        Nd4jMatrix matrix = new Nd4jMatrix(array);
        return matrix;
    }

}
