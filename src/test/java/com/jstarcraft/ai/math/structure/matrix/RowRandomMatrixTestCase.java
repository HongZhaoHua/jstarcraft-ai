package com.jstarcraft.ai.math.structure.matrix;

import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.RandomUtility;

import it.unimi.dsi.fastutil.ints.Int2FloatAVLTreeMap;

public class RowRandomMatrixTestCase extends RandomMatrixTestCase {

	@Test
	public void testDefault() {
		int dimension = 10;
		RandomMatrix matrix = RandomMatrix.valueOf(true, dimension, dimension, new Int2FloatAVLTreeMap());
		Assert.assertTrue(Float.isNaN(matrix.getValue(0, 0)));
	}

	@Override
	protected RandomMatrix getRandomMatrix(int dimension) {
		RandomMatrix matrix = RandomMatrix.valueOf(true, dimension, dimension, new Int2FloatAVLTreeMap());
		for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
			for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
				if (RandomUtility.randomBoolean()) {
					matrix.setValue(rowIndex, columnIndex, 0F);
				}
			}
		}
		return matrix;
	}

	@Override
	protected RandomMatrix getZeroMatrix(int dimension) {
		RandomMatrix matrix = RandomMatrix.valueOf(true, dimension, dimension, new Int2FloatAVLTreeMap());
		for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
			for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
				matrix.setValue(rowIndex, columnIndex, 0F);
			}
		}
		return matrix;
	}

	@Override
	public void testProduct() throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager")
				? EnvironmentContext.CPU
				: EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {
			int dimension = 10;
			MathMatrix leftMatrix = getRandomMatrix(dimension);
			MathMatrix rightMatrix = getRandomMatrix(dimension);
			MathMatrix dataMatrix = getZeroMatrix(dimension);
			MathMatrix labelMatrix = DenseMatrix.valueOf(dimension, dimension);
			MathVector dataVector = dataMatrix.getRowVector(0);
			MathVector labelVector = labelMatrix.getRowVector(0);

			// 相当于transposeProductThis
			labelMatrix.dotProduct(leftMatrix, false, leftMatrix, true, MathCalculator.SERIAL);
			dataMatrix.dotProduct(leftMatrix, false, leftMatrix, true, MathCalculator.SERIAL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));
			dataMatrix.dotProduct(leftMatrix, false, leftMatrix, true, MathCalculator.PARALLEL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));

			// 相当于transposeProductThat
			labelMatrix.dotProduct(leftMatrix, false, rightMatrix, true, MathCalculator.SERIAL);
			dataMatrix.dotProduct(leftMatrix, false, rightMatrix, true, MathCalculator.SERIAL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));
			dataMatrix.dotProduct(leftMatrix, false, rightMatrix, true, MathCalculator.PARALLEL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));

			MathVector leftVector = leftMatrix.getRowVector(RandomUtility.randomInteger(dimension));
			MathVector rightVector = rightMatrix.getRowVector(RandomUtility.randomInteger(dimension));
			labelMatrix.dotProduct(leftVector, rightVector, MathCalculator.SERIAL);
			dataMatrix.dotProduct(leftVector, rightVector, MathCalculator.SERIAL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));
			dataMatrix.dotProduct(leftVector, rightVector, MathCalculator.PARALLEL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));

			labelVector.dotProduct(leftMatrix, false, rightVector, MathCalculator.SERIAL);
			dataVector.dotProduct(leftMatrix, false, rightVector, MathCalculator.SERIAL);
			Assert.assertTrue(equalVector(dataVector, labelVector));
			dataVector.dotProduct(leftMatrix, false, rightVector, MathCalculator.PARALLEL);
			Assert.assertTrue(equalVector(dataVector, labelVector));

			labelVector.dotProduct(leftVector, rightMatrix, true, MathCalculator.SERIAL);
			dataVector.dotProduct(leftVector, rightMatrix, true, MathCalculator.SERIAL);
			Assert.assertTrue(equalVector(dataVector, labelVector));
			dataVector.dotProduct(leftVector, rightMatrix, true, MathCalculator.PARALLEL);
			Assert.assertTrue(equalVector(dataVector, labelVector));

			// 利用转置乘运算的对称性
			dataMatrix = new SymmetryMatrix(dimension);
			labelMatrix.dotProduct(leftMatrix, false, leftMatrix, true, MathCalculator.SERIAL);
			dataMatrix.dotProduct(leftMatrix, false, leftMatrix, true, MathCalculator.SERIAL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));
		});
	}

}
