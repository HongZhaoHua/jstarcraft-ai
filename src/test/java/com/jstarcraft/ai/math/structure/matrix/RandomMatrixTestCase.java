package com.jstarcraft.ai.math.structure.matrix;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

public abstract class RandomMatrixTestCase extends MatrixTestCase {

	@Override
	protected abstract RandomMatrix getRandomMatrix(int dimension);

	@Override
	protected abstract RandomMatrix getZeroMatrix(int dimension);

	@Test
	public void testNotify() {
		AtomicInteger oldSize = new AtomicInteger();
		AtomicInteger newSize = new AtomicInteger();
		int dimension = 10;
		RandomMatrix matrix = getRandomMatrix(dimension);
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
