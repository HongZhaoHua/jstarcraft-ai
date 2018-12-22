package com.jstarcraft.ai.neuralnetwork.vertex.transformation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.RowCompositeMatrix;
import com.jstarcraft.ai.model.ModelDefinition;
import com.jstarcraft.ai.neuralnetwork.MatrixFactory;
import com.jstarcraft.ai.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.core.utility.KeyValue;

/**
 * UnstackVertex allows for unstacking of inputs so that they may be forwarded
 * through a network. This is useful for cases such as Triplet Embedding, where
 * embeddings can be separated and run through subsequent layers.
 *
 * Works similarly to SubsetVertex, except on dimension 0 of the input.
 * stackSize is explicitly defined by the user to properly calculate an step.
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
@ModelDefinition(value = { "vertexName", "factory", "from", "to" })
// TODO 准备改名为VerticalDetachVertex
public class VerticalUnstackVertex extends AbstractVertex {

	// inclusive
	private int from;

	// exclusive
	private int to;

	protected VerticalUnstackVertex() {
	}

	public VerticalUnstackVertex(String name, MatrixFactory factory, int from, int to) {
		super(name, factory);
		this.from = from;
		this.to = to;
	}

	@Override
	public void doCache(KeyValue<MathMatrix, MathMatrix>... samples) {
		super.doCache(samples);

		// 获取样本的数量与维度
		MathMatrix outputData = RowCompositeMatrix.detachOf(RowCompositeMatrix.class.cast(samples[0].getKey()), from, to);
		outputKeyValue.setKey(outputData);
		MathMatrix innerError = RowCompositeMatrix.detachOf(RowCompositeMatrix.class.cast(samples[0].getValue()), from, to);
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
			VerticalUnstackVertex that = (VerticalUnstackVertex) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.vertexName, that.vertexName);
			equal.append(this.from, that.from);
			equal.append(this.to, that.to);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(vertexName);
		hash.append(from);
		hash.append(to);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "UnstackVertex(name=" + vertexName + ", from=" + from + ", to=" + to + ")";
	}

}
