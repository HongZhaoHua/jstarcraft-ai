package com.jstarcraft.ai.math.algorithm.similarity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;
import com.jstarcraft.ai.math.structure.matrix.SparseMatrix;
import com.jstarcraft.ai.math.structure.matrix.SymmetryMatrix;
import com.jstarcraft.core.utility.RandomUtility;

public abstract class AbstractSimilarityTestCase {

	protected abstract boolean checkCorrelation(float correlation);

	protected abstract float getIdentical();

	protected abstract Similarity getSimilarity();

	@Test
	public void test() {
		int rowSize = 50;
		int columnSize = 100;
		Table<Integer, Integer, Float> table = HashBasedTable.create();
		for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
			for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
				if (RandomUtility.randomBoolean()) {
					table.put(rowIndex, columnIndex, RandomUtility.randomFloat(1F));
				}
			}
		}
		SparseMatrix scoreMatrix = SparseMatrix.valueOf(rowSize, columnSize, table);

		Similarity similarity = getSimilarity();
		SymmetryMatrix similarityMatrix = similarity.makeSimilarityMatrix(scoreMatrix, false, 0F);
		assertEquals(rowSize, similarityMatrix.getRowSize());
		for (MatrixScalar term : similarityMatrix) {
			assertTrue(checkCorrelation(term.getValue()));
		}
		for (int index = 0, size = rowSize; index < size; index++) {
			Assert.assertThat(similarityMatrix.getValue(index, index), CoreMatchers.equalTo(getIdentical()));
		}

		similarityMatrix = similarity.makeSimilarityMatrix(scoreMatrix, true, 0F);
		assertEquals(columnSize, similarityMatrix.getRowSize());
		for (MatrixScalar term : similarityMatrix) {
			assertTrue(checkCorrelation(term.getValue()));
		}
		for (int index = 0, size = columnSize; index < size; index++) {
			Assert.assertThat(similarityMatrix.getValue(index, index), CoreMatchers.equalTo(getIdentical()));
		}
	}

}
