package com.jstarcraft.ai.utility;

import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.factory.Nd4j;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;
import com.jstarcraft.ai.math.structure.matrix.SparseMatrix;

import it.unimi.dsi.fastutil.floats.Float2IntAVLTreeMap;
import it.unimi.dsi.fastutil.floats.Float2IntSortedMap;

public class SearchUtilityTestCase {

	@Test
	public void testPageRank() throws Exception {
		testPageRank(MathCalculator.SERIAL);
		testPageRank(MathCalculator.PARALLEL);
	}

	private void testPageRank(MathCalculator mode) throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {
			int dimension = 7;
			Table<Integer, Integer, Float> table = HashBasedTable.create();
			table.put(0, 1, 0.5F);
			table.put(0, 2, 0.5F);

			table.put(2, 0, 0.3F);
			table.put(2, 1, 0.3F);
			table.put(2, 4, 0.3F);

			table.put(3, 4, 0.5F);
			table.put(3, 5, 0.5F);

			table.put(4, 3, 0.5F);
			table.put(4, 5, 0.5F);

			table.put(5, 3, 1F);

			table.put(6, 1, 0.5F);
			table.put(6, 3, 0.5F);
			SparseMatrix sparseMatrix = SparseMatrix.valueOf(dimension, dimension, table);
			DenseMatrix denseMatrix = DenseMatrix.valueOf(dimension, dimension);
			for (MatrixScalar scalar : sparseMatrix) {
				denseMatrix.setValue(scalar.getRow(), scalar.getColumn(), scalar.getValue());
			}

			Float2IntSortedMap sparseSort = new Float2IntAVLTreeMap();
			{
				int index = 0;
				for (float score : SearchUtility.pageRank(mode, dimension, sparseMatrix)) {
					sparseSort.put(score, index++);
				}
			}
			Assert.assertArrayEquals(new int[] { 6, 0, 2, 1, 4, 5, 3 }, sparseSort.values().toIntArray());

			Float2IntSortedMap denseSort = new Float2IntAVLTreeMap();
			{
				int index = 0;
				for (float score : SearchUtility.pageRank(mode, dimension, denseMatrix)) {
					denseSort.put(score, index++);
				}
			}
			Assert.assertArrayEquals(new int[] { 6, 0, 2, 1, 4, 5, 3 }, denseSort.values().toIntArray());

			Assert.assertTrue(sparseSort.equals(denseSort));
		});
		task.get();
	}

}
