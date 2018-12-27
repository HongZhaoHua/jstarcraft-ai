package com.jstarcraft.ai.neuralnetwork;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.Nd4jMatrix;

public class Nd4jMatrixFactory implements MatrixFactory {

	@Override
	public MathMatrix makeCache(int rowSize, int columnSize) {
		INDArray array = Nd4j.zeros(rowSize, columnSize);
		Nd4jMatrix matrix = new Nd4jMatrix(array);
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
		return "Nd4jMatrixFactory()";
	}

}
