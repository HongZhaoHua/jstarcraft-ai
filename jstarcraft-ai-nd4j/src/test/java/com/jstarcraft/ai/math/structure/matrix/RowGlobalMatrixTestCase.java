package com.jstarcraft.ai.math.structure.matrix;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.core.utility.RandomUtility;

import it.unimi.dsi.fastutil.longs.Long2FloatRBTreeMap;

public class RowGlobalMatrixTestCase extends MatrixTestCase {

    @Override
    protected RowGlobalMatrix getRandomMatrix(int dimension) {
        MathMatrix from = DenseMatrix.valueOf(1, dimension);
        HashMatrix table = new HashMatrix(true, dimension, dimension, new Long2FloatRBTreeMap());
        for (int rowIndex = 0; rowIndex < dimension - 1; rowIndex++) {
            for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
                if (RandomUtility.randomBoolean()) {
                    table.setValue(rowIndex, columnIndex, 0F);
                }
            }
        }
        MathMatrix to = SparseMatrix.valueOf(dimension - 1, dimension, table);
        RowGlobalMatrix matrix = RowGlobalMatrix.attachOf(from, to);
        matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            scalar.setValue(RandomUtility.randomInteger(dimension));
        });
        return matrix;
    }

    @Override
    protected RowGlobalMatrix getZeroMatrix(int dimension) {
        MathMatrix from = DenseMatrix.valueOf(1, dimension);
        HashMatrix table = new HashMatrix(true, dimension, dimension, new Long2FloatRBTreeMap());
        for (int rowIndex = 0; rowIndex < dimension - 1; rowIndex++) {
            for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
                table.setValue(rowIndex, columnIndex, 0F);
            }
        }
        MathMatrix to = SparseMatrix.valueOf(dimension - 1, dimension, table);
        RowGlobalMatrix matrix = RowGlobalMatrix.attachOf(from, to);
        return matrix;
    }

    @Test
    public void testComponent() {
        int dimension = 10;
        RowGlobalMatrix matrix = getRandomMatrix(dimension);

        Assert.assertThat(matrix.getComponentSize(), CoreMatchers.equalTo(2));
        Assert.assertFalse(matrix.isIndexed());

        matrix = RowGlobalMatrix.attachOf(matrix, matrix);
        Assert.assertThat(matrix.getComponentSize(), CoreMatchers.equalTo(4));
        Assert.assertTrue(matrix.getComponentMatrix(0) instanceof DenseMatrix);
        Assert.assertTrue(matrix.getComponentMatrix(2) instanceof DenseMatrix);
        Assert.assertTrue(matrix.getComponentMatrix(0) == matrix.getComponentMatrix(2));

        matrix = RowGlobalMatrix.detachOf(getRandomMatrix(dimension), 0, 1);
        Assert.assertThat(matrix.getComponentSize(), CoreMatchers.equalTo(1));
        Assert.assertTrue(matrix.isIndexed());

        matrix = RowGlobalMatrix.detachOf(getRandomMatrix(dimension), 1, dimension);
        Assert.assertThat(matrix.getComponentSize(), CoreMatchers.equalTo(1));
        Assert.assertFalse(matrix.isIndexed());
    }

}
