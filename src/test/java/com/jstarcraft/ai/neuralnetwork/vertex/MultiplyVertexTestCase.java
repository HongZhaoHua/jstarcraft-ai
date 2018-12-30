package com.jstarcraft.ai.neuralnetwork.vertex;

import org.deeplearning4j.nn.graph.vertex.GraphVertex;
import org.deeplearning4j.nn.graph.vertex.impl.ElementWiseVertex;
import org.deeplearning4j.nn.graph.vertex.impl.ElementWiseVertex.Op;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.math.structure.DenseCache;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.neuralnetwork.vertex.operation.MultiplyVertex;

public class MultiplyVertexTestCase extends VertexTestCase {

	@Override
	protected INDArray getError() {
		return Nd4j.linspace(-2.5D, 2.5D, 10).reshape(5, 2);
	}

	@Override
	protected int getSize() {
		return 2;
	}

	@Override
	protected GraphVertex getOldFunction() {
		return new ElementWiseVertex(null, "old", 0, Op.Product);
	}

	@Override
	protected Vertex getNewFunction() {
		MathCache cache = new DenseCache();
		return new MultiplyVertex("new", cache);
	}

}
