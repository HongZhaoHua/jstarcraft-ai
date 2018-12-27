package com.jstarcraft.ai.math.structure.matrix;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.core.utility.RandomUtility;

public class RowCompositeMatrixTestCase extends MatrixTestCase {

	@Override
	protected RowCompositeMatrix getRandomMatrix(int dimension) {
		MathMatrix from = DenseMatrix.valueOf(1, dimension);
		Table<Integer, Integer, Float> data = HashBasedTable.create();
		for (int rowIndex = 0; rowIndex < dimension - 1; rowIndex++) {
			for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
				if (RandomUtility.randomBoolean()) {
					data.put(rowIndex, columnIndex, 0F);
				}
			}
		}
		MathMatrix to = SparseMatrix.valueOf(dimension - 1, dimension, data);
		RowCompositeMatrix matrix = RowCompositeMatrix.attachOf(from, to);
		matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			scalar.setValue(RandomUtility.randomInteger(dimension));
		});
		return matrix;
	}

	@Override
	protected RowCompositeMatrix getZeroMatrix(int dimension) {
		MathMatrix from = DenseMatrix.valueOf(1, dimension);
		Table<Integer, Integer, Float> data = HashBasedTable.create();
		for (int rowIndex = 0; rowIndex < dimension - 1; rowIndex++) {
			for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
				data.put(rowIndex, columnIndex, 0F);
			}
		}
		MathMatrix to = SparseMatrix.valueOf(dimension - 1, dimension, data);
		RowCompositeMatrix matrix = RowCompositeMatrix.attachOf(from, to);
		return matrix;
	}

	@Test
	public void testComponent() {
		int dimension = 10;
		RowCompositeMatrix matrix = getRandomMatrix(dimension);

		Assert.assertThat(matrix.getComponentSize(), CoreMatchers.equalTo(2));
		Assert.assertFalse(matrix.isIndexed());

		matrix = RowCompositeMatrix.attachOf(matrix, matrix);
		Assert.assertThat(matrix.getComponentSize(), CoreMatchers.equalTo(4));
		Assert.assertTrue(matrix.getComponentMatrix(0) instanceof DenseMatrix);
		Assert.assertTrue(matrix.getComponentMatrix(2) instanceof DenseMatrix);
		Assert.assertTrue(matrix.getComponentMatrix(0) == matrix.getComponentMatrix(2));

		matrix = RowCompositeMatrix.detachOf(getRandomMatrix(dimension), 0, 1);
		Assert.assertThat(matrix.getComponentSize(), CoreMatchers.equalTo(1));
		Assert.assertTrue(matrix.isIndexed());

		matrix = RowCompositeMatrix.detachOf(getRandomMatrix(dimension), 1, dimension);
		Assert.assertThat(matrix.getComponentSize(), CoreMatchers.equalTo(1));
		Assert.assertFalse(matrix.isIndexed());
	}

}
