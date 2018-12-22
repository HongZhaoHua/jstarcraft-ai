package com.jstarcraft.ai.neuralnetwork.vertex;

import org.deeplearning4j.nn.graph.vertex.GraphVertex;
import org.deeplearning4j.nn.graph.vertex.impl.SubsetVertex;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.ColumnCompositeMatrix;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.neuralnetwork.DenseMatrixFactory;
import com.jstarcraft.ai.neuralnetwork.MatrixFactory;
import com.jstarcraft.ai.neuralnetwork.vertex.transformation.HorizontalUnstackVertex;

public class HorizontalUnstackVertexTestCase extends VertexTestCase {

	@Override
	protected MathMatrix getMatrix(INDArray array) {
		int size = array.columns();
		MathMatrix[] components = new MathMatrix[size];
		for (int index = 0; index < size; index++) {
			components[index] = DenseMatrix.valueOf(array.rows(), 1);
		}
		MathMatrix matrix = ColumnCompositeMatrix.attachOf(components);
		matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			scalar.setValue(array.getFloat(scalar.getRow(), scalar.getColumn()));
		});
		return matrix;
	}

	@Override
	protected INDArray getError() {
		return Nd4j.linspace(-2.5D, 2.5D, 5).reshape(5, 1);
	}

	@Override
	protected int getSize() {
		return 1;
	}

	@Override
	protected GraphVertex getOldFunction() {
		return new SubsetVertex(null, "old", 0, 1, 2);
	}

	@Override
	protected Vertex getNewFunction() {
		MatrixFactory cache = new DenseMatrixFactory();
		return new HorizontalUnstackVertex("new", cache, 1, 2);
	}

}
