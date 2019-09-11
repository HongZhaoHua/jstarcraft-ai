package com.jstarcraft.ai.model.neuralnetwork.vertex;

import org.deeplearning4j.nn.graph.vertex.GraphVertex;
import org.deeplearning4j.nn.graph.vertex.impl.ElementWiseVertex;
import org.deeplearning4j.nn.graph.vertex.impl.ElementWiseVertex.Op;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.math.structure.DenseCache;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.model.neuralnetwork.vertex.Vertex;
import com.jstarcraft.ai.model.neuralnetwork.vertex.operation.PlusVertex;

public class PlusVertexTestCase extends VertexTestCase {

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
        return new ElementWiseVertex(null, "old", 0, Op.Add);
    }

    @Override
    protected Vertex getNewFunction() {
        MathCache cache = new DenseCache();
        return new PlusVertex("new", cache);
    }

}
