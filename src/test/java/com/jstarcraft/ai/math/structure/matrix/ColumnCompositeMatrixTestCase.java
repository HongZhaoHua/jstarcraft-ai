package com.jstarcraft.ai.math.structure.matrix;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.core.utility.RandomUtility;

public class ColumnCompositeMatrixTestCase extends MatrixTestCase {

	@Override
	protected ColumnCompositeMatrix getRandomMatrix(int dimension) {
		MathMatrix from = DenseMatrix.valueOf(dimension, 1);
		Table<Integer, Integer, Float> table = HashBasedTable.create();
		for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
			for (int columnIndex = 0; columnIndex < dimension - 1; columnIndex++) {
				if (RandomUtility.randomBoolean()) {
					table.put(rowIndex, columnIndex, 0F);
				}
			}
		}
		MathMatrix to = SparseMatrix.valueOf(dimension, dimension - 1, table);
		ColumnCompositeMatrix matrix = ColumnCompositeMatrix.attachOf(from, to);
		matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			scalar.setValue(RandomUtility.randomInteger(dimension));
		});
		return matrix;
	}

	@Override
	protected ColumnCompositeMatrix getZeroMatrix(int dimension) {
		MathMatrix from = DenseMatrix.valueOf(dimension, 1);
		Table<Integer, Integer, Float> data = HashBasedTable.create();
		for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
			for (int columnIndex = 0; columnIndex < dimension - 1; columnIndex++) {
				data.put(rowIndex, columnIndex, 0F);
			}
		}
		MathMatrix to = SparseMatrix.valueOf(dimension, dimension - 1, data);
		ColumnCompositeMatrix matrix = ColumnCompositeMatrix.attachOf(from, to);
		return matrix;
	}

	@Test
	public void testComponent() {
		int dimension = 10;
		ColumnCompositeMatrix matrix = getRandomMatrix(dimension);

		Assert.assertThat(matrix.getComponentSize(), CoreMatchers.equalTo(2));
		Assert.assertFalse(matrix.isIndexed());

		matrix = ColumnCompositeMatrix.attachOf(matrix, matrix);
		Assert.assertThat(matrix.getComponentSize(), CoreMatchers.equalTo(4));
		Assert.assertTrue(matrix.getComponentMatrix(0) instanceof DenseMatrix);
		Assert.assertTrue(matrix.getComponentMatrix(2) instanceof DenseMatrix);
		Assert.assertTrue(matrix.getComponentMatrix(0) == matrix.getComponentMatrix(2));

		matrix = ColumnCompositeMatrix.detachOf(getRandomMatrix(dimension), 0, 1);
		Assert.assertThat(matrix.getComponentSize(), CoreMatchers.equalTo(1));
		Assert.assertTrue(matrix.isIndexed());

		matrix = ColumnCompositeMatrix.detachOf(getRandomMatrix(dimension), 1, dimension);
		Assert.assertThat(matrix.getComponentSize(), CoreMatchers.equalTo(1));
		Assert.assertFalse(matrix.isIndexed());
	}

}
