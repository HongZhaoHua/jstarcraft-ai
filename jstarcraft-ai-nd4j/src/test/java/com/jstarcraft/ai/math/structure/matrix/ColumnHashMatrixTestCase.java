package com.jstarcraft.ai.math.structure.matrix;

import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentFactory;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.RandomUtility;

import it.unimi.dsi.fastutil.longs.Long2FloatRBTreeMap;

public class ColumnHashMatrixTestCase extends HashMatrixTestCase {

    @Test
    public void testDefault() {
        int dimension = 10;
        HashMatrix matrix = new HashMatrix(false, dimension, dimension, new Long2FloatRBTreeMap());
        Assert.assertTrue(Float.isNaN(matrix.getValue(0, 0)));
    }

    @Override
    protected HashMatrix getRandomMatrix(int dimension) {
        HashMatrix matrix = new HashMatrix(false, dimension, dimension, new Long2FloatRBTreeMap());
        for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
            for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
                if (RandomUtility.randomBoolean()) {
                    matrix.setValue(rowIndex, columnIndex, 0F);
                }
            }
        }
        return matrix;
    }

    @Override
    protected HashMatrix getZeroMatrix(int dimension) {
        HashMatrix matrix = new HashMatrix(false, dimension, dimension, new Long2FloatRBTreeMap());
        for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
            for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
                matrix.setValue(rowIndex, columnIndex, 0F);
            }
        }
        return matrix;
    }

    @Override
    public void testProduct() throws Exception {
        EnvironmentContext context = EnvironmentFactory.getContext();
        Future<?> task = context.doTask(() -> {
            int dimension = 10;
            MathMatrix leftMatrix = getRandomMatrix(dimension);
            MathMatrix rightMatrix = getRandomMatrix(dimension);
            MathMatrix dataMatrix = getZeroMatrix(dimension);
            MathMatrix markMatrix = DenseMatrix.valueOf(dimension, dimension);
            MathVector dataVector = dataMatrix.getColumnVector(0);
            MathVector markVector = markMatrix.getColumnVector(0);

            // 相当于transposeProductThis
            markMatrix.dotProduct(leftMatrix, true, leftMatrix, false, MathCalculator.SERIAL);
            dataMatrix.dotProduct(leftMatrix, true, leftMatrix, false, MathCalculator.SERIAL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));
            dataMatrix.dotProduct(leftMatrix, true, leftMatrix, false, MathCalculator.PARALLEL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));

            // 相当于transposeProductThat
            markMatrix.dotProduct(leftMatrix, true, rightMatrix, false, MathCalculator.SERIAL);
            dataMatrix.dotProduct(leftMatrix, true, rightMatrix, false, MathCalculator.SERIAL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));
            dataMatrix.dotProduct(leftMatrix, true, rightMatrix, false, MathCalculator.PARALLEL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));

            MathVector leftVector = leftMatrix.getColumnVector(RandomUtility.randomInteger(dimension));
            MathVector rightVector = rightMatrix.getColumnVector(RandomUtility.randomInteger(dimension));
            markMatrix.dotProduct(leftVector, rightVector, MathCalculator.SERIAL);
            dataMatrix.dotProduct(leftVector, rightVector, MathCalculator.SERIAL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));
            dataMatrix.dotProduct(leftVector, rightVector, MathCalculator.PARALLEL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));

            markVector.dotProduct(leftMatrix, true, rightVector, MathCalculator.SERIAL);
            dataVector.dotProduct(leftMatrix, true, rightVector, MathCalculator.SERIAL);
            Assert.assertTrue(equalVector(dataVector, markVector));
            dataVector.dotProduct(leftMatrix, true, rightVector, MathCalculator.PARALLEL);
            Assert.assertTrue(equalVector(dataVector, markVector));

            markVector.dotProduct(leftVector, rightMatrix, false, MathCalculator.SERIAL);
            dataVector.dotProduct(leftVector, rightMatrix, false, MathCalculator.SERIAL);
            Assert.assertTrue(equalVector(dataVector, markVector));
            dataVector.dotProduct(leftVector, rightMatrix, false, MathCalculator.PARALLEL);
            Assert.assertTrue(equalVector(dataVector, markVector));

            // 利用转置乘运算的对称性
            dataMatrix = new SymmetryMatrix(dimension);
            markMatrix.dotProduct(leftMatrix, true, leftMatrix, false, MathCalculator.SERIAL);
            dataMatrix.dotProduct(leftMatrix, true, leftMatrix, false, MathCalculator.SERIAL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));
        });
        task.get();
    }

}
