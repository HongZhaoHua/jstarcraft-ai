package com.jstarcraft.ai.search;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;

import it.unimi.dsi.fastutil.floats.Float2IntAVLTreeMap;
import it.unimi.dsi.fastutil.floats.Float2IntSortedMap;

public class PageRankTestCase {

	@Test
	public void test() throws Exception {
		int dimension = 7;
		DenseMatrix matrix = DenseMatrix.valueOf(dimension, dimension);
		matrix.setValue(0, 1, 0.5F);
		matrix.setValue(0, 2, 0.5F);

		matrix.setValue(2, 0, 0.3F);
		matrix.setValue(2, 1, 0.3F);
		matrix.setValue(2, 4, 0.3F);

		matrix.setValue(3, 4, 0.5F);
		matrix.setValue(3, 5, 0.5F);

		matrix.setValue(4, 3, 0.5F);
		matrix.setValue(4, 5, 0.5F);

		matrix.setValue(5, 3, 1F);

		matrix.setValue(6, 1, 0.5F);
		matrix.setValue(6, 3, 0.5F);

		PageRank pageRank = new PageRank(dimension, matrix);
		pageRank.findPageRank();

		Float2IntSortedMap sort = new Float2IntAVLTreeMap();
		for (int index = 0; index < dimension; index++) {
			sort.put(pageRank.getPageRank(index), index);
		}
		Assert.assertArrayEquals(new int[] { 6, 0, 2, 1, 4, 5, 3 }, sort.values().toIntArray());
	}

}
