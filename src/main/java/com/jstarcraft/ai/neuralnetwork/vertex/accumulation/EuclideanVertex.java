package com.jstarcraft.ai.neuralnetwork.vertex.accumulation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;

import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.ModelDefinition;
import com.jstarcraft.ai.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.core.utility.KeyValue;

/**
 * Euclidean节点
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
@ModelDefinition(value = { "vertexName", "factory", "epsilon" })
public class EuclideanVertex extends AbstractVertex {

	public static final float DEFAULT_EPSILON = 1E-5F;

	private float epsilon;

	private int columnSize;

	private MathMatrix differences;

	protected EuclideanVertex() {
	}

	public EuclideanVertex(String name, MathCache factory) {
		this(name, factory, DEFAULT_EPSILON);
	}

	public EuclideanVertex(String name, MathCache factory, float epsilon) {
		super(name, factory);
		this.epsilon = epsilon;
	}

	@Override
	public void doCache(KeyValue<MathMatrix, MathMatrix>... samples) {
		super.doCache(samples);

		// 检查样本的数量是否一样
		int rowSize = samples[0].getKey().getRowSize();
		for (int position = 1; position < samples.length; position++) {
			if (rowSize != samples[position].getKey().getRowSize()) {
				throw new IllegalArgumentException();
			}
		}

		// 检查样本的维度是否一样
		columnSize = samples[0].getKey().getColumnSize();
		for (int position = 1; position < samples.length; position++) {
			if (columnSize != samples[position].getKey().getColumnSize()) {
				throw new IllegalArgumentException();
			}
		}

		MathMatrix outputData = factory.makeMatrix(rowSize, 1);
		outputKeyValue.setKey(outputData);
		MathMatrix innerError = factory.makeMatrix(rowSize, 1);
		outputKeyValue.setValue(innerError);

		differences = factory.makeMatrix(rowSize, columnSize);
	}

	@Override
	public void doForward() {
		MathMatrix outputData = outputKeyValue.getKey();
		MathMatrix leftInputData = inputKeyValues[0].getKey();
		MathMatrix rightInputData = inputKeyValues[1].getKey();
		for (int rowIndex = 0, rowSize = outputData.getRowSize(); rowIndex < rowSize; rowIndex++) {
			float value = 0F;
			for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
				float difference = leftInputData.getValue(rowIndex, columnIndex) - rightInputData.getValue(rowIndex, columnIndex);
				// 缓存
				differences.setValue(rowIndex, columnIndex, difference);
				value += FastMath.pow(difference, 2);
			}
			outputData.setValue(rowIndex, 0, (float) FastMath.sqrt(value));
		}
		MathMatrix innerError = outputKeyValue.getValue();
		innerError.setValues(0F);
	}

	@Override
	public void doBackward() {
		MathMatrix outputData = outputKeyValue.getKey();
		MathMatrix innerError = outputKeyValue.getValue();

		MathMatrix leftInputError = inputKeyValues[0].getValue();
		MathMatrix rightInputError = inputKeyValues[1].getValue();
		for (int rowIndex = 0, rowSize = innerError.getRowSize(); rowIndex < rowSize; rowIndex++) {
			// innerError / outputData
			float error = outputData.getValue(rowIndex, 0);
			error = error < epsilon ? epsilon : error;
			error = innerError.getValue(rowIndex, 0) / error;
			for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
				float value = differences.getValue(rowIndex, columnIndex) * error;
				if (leftInputError != null) {
					// TODO 使用累计的方式计算
					// TODO 需要锁机制,否则并发计算会导致Bug
					leftInputError.shiftValue(rowIndex, columnIndex, value);
				}
				if (rightInputError != null) {
					// TODO 使用累计的方式计算
					// TODO 需要锁机制,否则并发计算会导致Bug
					rightInputError.shiftValue(rowIndex, columnIndex, -value);
				}
			}
		}
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
			EuclideanVertex that = (EuclideanVertex) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.vertexName, that.vertexName);
			equal.append(this.epsilon, that.epsilon);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(vertexName);
		hash.append(epsilon);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "EuclideanVertex(name=" + vertexName + ")";
	}

}
