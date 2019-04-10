package com.jstarcraft.ai.math.structure.matrix;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

public abstract class HashMatrixTestCase extends MatrixTestCase {

	@Override
	protected abstract HashMatrix getRandomMatrix(int dimension);

	@Override
	protected abstract HashMatrix getZeroMatrix(int dimension);

	@Test
	public void testNotify() {
		AtomicInteger oldSize = new AtomicInteger();
		AtomicInteger newSize = new AtomicInteger();
		int dimension = 10;
		HashMatrix matrix = getRandomMatrix(dimension);
		int size = matrix.getElementSize();
		matrix.attachMonitor((iterator, oldElementSize, newElementSize, oldKnownSize, newKnownSize, oldUnknownSize, newUnknownSize) -> {
			oldSize.set(oldElementSize);
			newSize.set(newElementSize);
		});

		matrix.setValues(Float.NaN);
		Assert.assertEquals(size, oldSize.get());
		Assert.assertEquals(0, newSize.get());
	}

}
