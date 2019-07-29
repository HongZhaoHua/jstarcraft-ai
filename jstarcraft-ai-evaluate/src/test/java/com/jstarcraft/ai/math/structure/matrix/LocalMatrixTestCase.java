package com.jstarcraft.ai.math.structure.matrix;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.core.utility.RandomUtility;

public class LocalMatrixTestCase extends MatrixTestCase {

	@Override
	protected LocalMatrix getRandomMatrix(int dimension) {
		LocalMatrix matrix = new LocalMatrix(DenseMatrix.valueOf(dimension * 3, dimension * 3), dimension, dimension * 2, dimension, dimension * 2);
		matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			scalar.setValue(RandomUtility.randomInteger(dimension));
		});
		return matrix;
	}

	@Override
	protected LocalMatrix getZeroMatrix(int dimension) {
		LocalMatrix matrix = new LocalMatrix(DenseMatrix.valueOf(dimension * 3, dimension * 3), dimension, dimension * 2, dimension, dimension * 2);
		return matrix;
	}

}
