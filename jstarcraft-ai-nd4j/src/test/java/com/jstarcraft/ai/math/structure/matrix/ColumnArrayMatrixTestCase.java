package com.jstarcraft.ai.math.structure.matrix;

import java.util.concurrent.Future;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentFactory;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.message.SumMessage;
import com.jstarcraft.ai.math.structure.vector.ArrayVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.RandomUtility;

import it.unimi.dsi.fastutil.longs.Long2FloatRBTreeMap;

public class ColumnArrayMatrixTestCase extends MatrixTestCase {

	@Override
	protected ColumnArrayMatrix getRandomMatrix(int dimension) {
		HashMatrix table = new HashMatrix(true, dimension, dimension, new Long2FloatRBTreeMap());
		for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
			for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
				if (RandomUtility.randomBoolean()) {
					table.setValue(rowIndex, columnIndex, 0F);
				}
			}
		}
		SparseMatrix data = SparseMatrix.valueOf(dimension, dimension, table);
		ArrayVector[] vectors = new ArrayVector[dimension];
		for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
			vectors[columnIndex] = new ArrayVector(data.getColumnVector(columnIndex));
		}
		ColumnArrayMatrix matrix = ColumnArrayMatrix.valueOf(dimension, vectors);
		matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			scalar.setValue(RandomUtility.randomInteger(dimension));
		});
		return matrix;
	}

	@Override
	protected ColumnArrayMatrix getZeroMatrix(int dimension) {
		HashMatrix table = new HashMatrix(true, dimension, dimension, new Long2FloatRBTreeMap());
		for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
			for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
				table.setValue(rowIndex, columnIndex, 0F);
			}
		}
		SparseMatrix data = SparseMatrix.valueOf(dimension, dimension, table);
		ArrayVector[] vectors = new ArrayVector[dimension];
		for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
			vectors[columnIndex] = new ArrayVector(data.getColumnVector(columnIndex));
		}
		ColumnArrayMatrix matrix = ColumnArrayMatrix.valueOf(dimension, vectors);
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

	@Test
	public void testNotify() {
		int dimension = 10;
		ColumnArrayMatrix matrix = getRandomMatrix(dimension);
		matrix.setValues(1F);

		try {
			matrix.getRowVector(RandomUtility.randomInteger(dimension));
			Assert.fail();
		} catch (UnsupportedOperationException exception) {
		}

		ArrayVector vector = matrix.getColumnVector(RandomUtility.randomInteger(dimension));
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
