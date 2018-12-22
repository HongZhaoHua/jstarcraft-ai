package com.jstarcraft.ai.neuralnetwork.vertex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.ModelDefinition;
import com.jstarcraft.ai.neuralnetwork.MatrixFactory;
import com.jstarcraft.core.utility.KeyValue;

/**
 * BaseGraphVertex defines a set of common functionality for GraphVertex
 * instances.
 */
/**
 * Euclidean节点
 * 
 * <pre></pre>
 * 
 * @author Birdy
 *
 */
@ModelDefinition(value = { "vertexName", "factory" })
public abstract class AbstractVertex implements Vertex {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected String vertexName;

	protected MatrixFactory factory;

	/** 键为inputData(不可以为null),值为outerError(可以为null) */
	protected KeyValue<MathMatrix, MathMatrix>[] inputKeyValues;

	/** 键为outputData(不可以为null),值为innerError(不可以为null) */
	protected KeyValue<MathMatrix, MathMatrix> outputKeyValue;

	protected AbstractVertex() {
	}

	protected AbstractVertex(String name, MatrixFactory factory) {
		this.vertexName = name;
		this.factory = factory;
	}

	@Override
	public void doCache(KeyValue<MathMatrix, MathMatrix>... samples) {
		// 检查样本
		if (samples.length == 0) {
			throw new IllegalArgumentException();
		}

		this.inputKeyValues = samples;
		this.outputKeyValue = new KeyValue<>(null, null);
	}

	@Override
	public String getVertexName() {
		return vertexName;
	}

	@Override
	public KeyValue<MathMatrix, MathMatrix> getInputKeyValue(int position) {
		return inputKeyValues[position];
	}

	@Override
	public KeyValue<MathMatrix, MathMatrix> getOutputKeyValue() {
		return outputKeyValue;
	}

	@Override
	public abstract String toString();

}
