package com.jstarcraft.ai.math.structure.matrix;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.core.utility.RandomUtility;

import it.unimi.dsi.fastutil.longs.Long2FloatRBTreeMap;

public class SparseMatrixTestCase extends MatrixTestCase {

    @Override
    protected SparseMatrix getRandomMatrix(int dimension) {
        HashMatrix table = new HashMatrix(true, dimension, dimension, new Long2FloatRBTreeMap());
        for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
            for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
                if (RandomUtility.randomBoolean()) {
                    table.setValue(rowIndex, columnIndex, 0F);
                }
            }
        }
        SparseMatrix matrix = SparseMatrix.valueOf(dimension, dimension, table);
        matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            scalar.setValue(RandomUtility.randomInteger(dimension));
        });
        return matrix;
    }

    @Override
    protected SparseMatrix getZeroMatrix(int dimension) {
        HashMatrix table = new HashMatrix(true, dimension, dimension, new Long2FloatRBTreeMap());
        for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
            for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
                table.setValue(rowIndex, columnIndex, 0F);
            }
        }
        SparseMatrix matrix = SparseMatrix.valueOf(dimension, dimension, table);
        return matrix;
    }

}
