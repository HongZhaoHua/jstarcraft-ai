package com.jstarcraft.ai.math.structure.matrix;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.core.utility.RandomUtility;

public class DenseMatrixTestCase extends MatrixTestCase {

    @Override
    protected DenseMatrix getRandomMatrix(int dimension) {
        DenseMatrix matrix = DenseMatrix.valueOf(dimension, dimension);
        matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            scalar.setValue(RandomUtility.randomInteger(dimension));
        });
        return matrix;
    }

    @Override
    protected DenseMatrix getZeroMatrix(int dimension) {
        DenseMatrix matrix = DenseMatrix.valueOf(dimension, dimension);
        return matrix;
    }

}
