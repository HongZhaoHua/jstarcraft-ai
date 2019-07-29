package com.jstarcraft.ai.model.neuralnetwork.vertex.transformation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.ColumnGlobalMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.core.utility.KeyValue;

/**
 * HorizontalAttachVertex节点
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class HorizontalAttachVertex extends AbstractVertex {

	protected HorizontalAttachVertex() {
	}

	public HorizontalAttachVertex(String name, MathCache factory) {
		super(name, factory);
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

		// 获取样本的维度
		MathMatrix[] keys = new MathMatrix[inputKeyValues.length];
		MathMatrix[] values = new MathMatrix[inputKeyValues.length];
		for (int position = 0; position < inputKeyValues.length; position++) {
			keys[position] = inputKeyValues[position].getKey();
			values[position] = inputKeyValues[position].getValue();
		}

		MathMatrix outputData = ColumnGlobalMatrix.attachOf(keys);
		outputKeyValue.setKey(outputData);
		MathMatrix innerError = ColumnGlobalMatrix.attachOf(values);
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
			HorizontalAttachVertex that = (HorizontalAttachVertex) object;
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
		return "HorizontalStackVertex(name=" + vertexName + ")";
	}

}
