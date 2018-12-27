package com.jstarcraft.ai.neuralnetwork.vertex.transformation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.RowCompositeMatrix;
import com.jstarcraft.ai.neuralnetwork.MatrixFactory;
import com.jstarcraft.ai.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.core.utility.KeyValue;

/**
 * StackVertex allows for stacking of inputs so that they may be forwarded
 * through a network. This is useful for cases such as Triplet Embedding, where
 * shared parameters are not supported by the network.
 *
 * This vertex will automatically stack all available inputs.
 *
 * @author Justin Long (crockpotveggies)
 */
/**
 * Euclidean节点
 * 
 * <pre></pre>
 * 
 * @author Birdy
 *
 */
// TODO 准备改名为VerticalAttachVertex
public class VerticalStackVertex extends AbstractVertex {

	protected VerticalStackVertex() {
	}

	public VerticalStackVertex(String name, MatrixFactory factory) {
		super(name, factory);
	}

	@Override
	public void doCache(KeyValue<MathMatrix, MathMatrix>... samples) {
		super.doCache(samples);

		// 检查样本的维度是否一样
		int columnSize = samples[0].getKey().getColumnSize();
		for (int position = 1; position < samples.length; position++) {
			if (columnSize != samples[position].getKey().getColumnSize()) {
				throw new IllegalArgumentException();
			}
		}

		// 获取样本的数量
		MathMatrix[] keys = new MathMatrix[inputKeyValues.length];
		MathMatrix[] values = new MathMatrix[inputKeyValues.length];
		for (int position = 0; position < inputKeyValues.length; position++) {
			keys[position] = inputKeyValues[position].getKey();
			values[position] = inputKeyValues[position].getValue();
		}

		MathMatrix outputData = RowCompositeMatrix.attachOf(keys);
		outputKeyValue.setKey(outputData);
		MathMatrix innerError = RowCompositeMatrix.attachOf(values);
		outputKeyValue.setValue(innerError);
	}

	@Override
	public void doForward() {
	}

	@Override
	public void doBackward() {
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
			VerticalStackVertex that = (VerticalStackVertex) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.vertexName, that.vertexName);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(vertexName);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "StackVertex(name=" + vertexName + ")";
	}

}
