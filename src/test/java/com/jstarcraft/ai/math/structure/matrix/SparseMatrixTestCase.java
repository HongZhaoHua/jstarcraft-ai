package com.jstarcraft.ai.math.structure.matrix;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.core.utility.RandomUtility;

public class SparseMatrixTestCase extends MatrixTestCase {

	@Override
	protected SparseMatrix getRandomMatrix(int dimension) {
		Table<Integer, Integer, Float> table = HashBasedTable.create();
		for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
			for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
				if (RandomUtility.randomBoolean()) {
					table.put(rowIndex, columnIndex, 0F);
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
		Table<Integer, Integer, Float> table = HashBasedTable.create();
		for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
			for (int columnIndex = 0; columnIndex < dimension; columnIndex++) {
				table.put(rowIndex, columnIndex, 0F);
			}
		}
		SparseMatrix matrix = SparseMatrix.valueOf(dimension, dimension, table);
		return matrix;
	}

}
