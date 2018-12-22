package com.jstarcraft.ai.math.structure.matrix;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.core.utility.RandomUtility;

public class SectionMatrixTestCase extends MatrixTestCase {

	@Override
	protected SectionMatrix getRandomMatrix(int dimension) {
		SectionMatrix matrix = new SectionMatrix(DenseMatrix.valueOf(dimension * 3, dimension * 3), dimension, dimension * 2, dimension, dimension * 2);
		matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			scalar.setValue(RandomUtility.randomInteger(dimension));
		});
		return matrix;
	}

	@Override
	protected SectionMatrix getZeroMatrix(int dimension) {
		SectionMatrix matrix = new SectionMatrix(DenseMatrix.valueOf(dimension * 3, dimension * 3), dimension, dimension * 2, dimension, dimension * 2);
		return matrix;
	}

}
