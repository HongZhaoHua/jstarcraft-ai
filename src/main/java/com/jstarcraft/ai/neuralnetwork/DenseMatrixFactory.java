package com.jstarcraft.ai.neuralnetwork;

import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

public class DenseMatrixFactory implements MatrixFactory {

	@Override
	public MathMatrix makeCache(int rowSize, int columnSize) {
		DenseMatrix matrix = DenseMatrix.valueOf(rowSize, columnSize);
		return matrix;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "DenseMatrixFactory()";
	}

}
