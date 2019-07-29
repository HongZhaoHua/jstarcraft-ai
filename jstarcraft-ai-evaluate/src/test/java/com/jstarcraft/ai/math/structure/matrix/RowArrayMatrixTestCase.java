package com.jstarcraft.ai.math.structure.matrix;

import java.util.concurrent.Future;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.message.SumMessage;
import com.jstarcraft.ai.math.structure.vector.ArrayVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.RandomUtility;

import it.unimi.dsi.fastutil.ints.Int2FloatRBTreeMap;

public class RowArrayMatrixTestCase extends MatrixTestCase {

    @Override
    protected RowArrayMatrix getRandomMatrix(int dimension) {
        HashMatrix table = new HashMatrix(true, dimension, dimension, new Int2FloatRBTreeMap());
        for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
            for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
                if (RandomUtility.randomBoolean()) {
                    table.setValue(rowIndex, columnIndex, 0F);
                }
            }
        }
        SparseMatrix data = SparseMatrix.valueOf(dimension, dimension, table);
        ArrayVector[] vectors = new ArrayVector[dimension];
        for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
            vectors[rowIndex] = new ArrayVector(data.getRowVector(rowIndex));
        }
        RowArrayMatrix matrix = RowArrayMatrix.valueOf(dimension, vectors);
        matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            scalar.setValue(RandomUtility.randomInteger(dimension));
        });
        return matrix;
    }

    @Override
    protected RowArrayMatrix getZeroMatrix(int dimension) {
        HashMatrix table = new HashMatrix(true, dimension, dimension, new Int2FloatRBTreeMap());
        for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
            for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
                table.setValue(rowIndex, columnIndex, 0F);
            }
        }
        SparseMatrix data = SparseMatrix.valueOf(dimension, dimension, table);
        ArrayVector[] vectors = new ArrayVector[dimension];
        for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
            vectors[rowIndex] = new ArrayVector(data.getRowVector(rowIndex));
        }
        RowArrayMatrix matrix = RowArrayMatrix.valueOf(dimension, vectors);
        return matrix;
    }

    @Override
    public void testProduct() throws Exception {
        EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
        Future<?> task = context.doTask(() -> {
            int dimension = 10;
            MathMatrix leftMatrix = getRandomMatrix(dimension);
            MathMatrix rightMatrix = getRandomMatrix(dimension);
            MathMatrix dataMatrix = getZeroMatrix(dimension);
            MathMatrix markMatrix = DenseMatrix.valueOf(dimension, dimension);
            MathVector dataVector = dataMatrix.getRowVector(0);
            MathVector markVector = markMatrix.getRowVector(0);

            // 相当于transposeProductThis
            markMatrix.dotProduct(leftMatrix, false, leftMatrix, true, MathCalculator.SERIAL);
            dataMatrix.dotProduct(leftMatrix, false, leftMatrix, true, MathCalculator.SERIAL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));
            dataMatrix.dotProduct(leftMatrix, false, leftMatrix, true, MathCalculator.PARALLEL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));

            // 相当于transposeProductThat
            markMatrix.dotProduct(leftMatrix, false, rightMatrix, true, MathCalculator.SERIAL);
            dataMatrix.dotProduct(leftMatrix, false, rightMatrix, true, MathCalculator.SERIAL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));
            dataMatrix.dotProduct(leftMatrix, false, rightMatrix, true, MathCalculator.PARALLEL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));

            MathVector leftVector = leftMatrix.getRowVector(RandomUtility.randomInteger(dimension));
            MathVector rightVector = rightMatrix.getRowVector(RandomUtility.randomInteger(dimension));
            markMatrix.dotProduct(leftVector, rightVector, MathCalculator.SERIAL);
            dataMatrix.dotProduct(leftVector, rightVector, MathCalculator.SERIAL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));
            dataMatrix.dotProduct(leftVector, rightVector, MathCalculator.PARALLEL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));

            markVector.dotProduct(leftMatrix, false, rightVector, MathCalculator.SERIAL);
            dataVector.dotProduct(leftMatrix, false, rightVector, MathCalculator.SERIAL);
            Assert.assertTrue(equalVector(dataVector, markVector));
            dataVector.dotProduct(leftMatrix, false, rightVector, MathCalculator.PARALLEL);
            Assert.assertTrue(equalVector(dataVector, markVector));

            markVector.dotProduct(leftVector, rightMatrix, true, MathCalculator.SERIAL);
            dataVector.dotProduct(leftVector, rightMatrix, true, MathCalculator.SERIAL);
            Assert.assertTrue(equalVector(dataVector, markVector));
            dataVector.dotProduct(leftVector, rightMatrix, true, MathCalculator.PARALLEL);
            Assert.assertTrue(equalVector(dataVector, markVector));

            // 利用转置乘运算的对称性
            dataMatrix = new SymmetryMatrix(dimension);
            markMatrix.dotProduct(leftMatrix, false, leftMatrix, true, MathCalculator.SERIAL);
            dataMatrix.dotProduct(leftMatrix, false, leftMatrix, true, MathCalculator.SERIAL);
            Assert.assertTrue(equalMatrix(dataMatrix, markMatrix));
        });
    }

    @Test
    public void testNotify() {
        int dimension = 10;
        RowArrayMatrix matrix = getRandomMatrix(dimension);
        matrix.setValues(1F);

        try {
            matrix.getColumnVector(RandomUtility.randomInteger(dimension));
            Assert.fail();
        } catch (UnsupportedOperationException exception) {
        }

        ArrayVector vector = matrix.getRowVector(RandomUtility.randomInteger(dimension));
        int oldSize = vector.getElementSize();
        int newSize = RandomUtility.randomInteger(oldSize);
        int[] indexes = new int[newSize];
        for (int index = 0; index < newSize; index++) {
            indexes[index] = index;
        }
        SumMessage message = new SumMessage(false);
        matrix.attachMonitor((iterator, oldElementSize, newElementSize, oldKnownSize, newKnownSize, oldUnknownSize, newUnknownSize) -> {
            Assert.assertThat(newElementSize - oldElementSize, CoreMatchers.equalTo(newSize - oldSize));
            message.accumulateValue(oldSize + newSize);
        });
        vector.modifyIndexes(indexes);
        vector.setValues(1F);
        Assert.assertThat(message.getValue(), CoreMatchers.equalTo(oldSize + newSize + 0F));
        Assert.assertThat(matrix.getSum(false), CoreMatchers.equalTo(matrix.getElementSize() + 0F));

        message.accumulateValue(-message.getValue());
        matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            message.accumulateValue(scalar.getValue());
        });
        Assert.assertThat(message.getValue(), CoreMatchers.equalTo(matrix.getSum(false)));

        message.accumulateValue(-message.getValue());
        for (MatrixScalar term : matrix) {
            message.accumulateValue(term.getValue());
        }
        Assert.assertThat(message.getValue(), CoreMatchers.equalTo(matrix.getSum(false)));
    }

}
