package com.jstarcraft.ai.neuralnetwork.vertex.transformation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.matrix.ColumnCompositeMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.ModelDefinition;
import com.jstarcraft.ai.neuralnetwork.MatrixFactory;
import com.jstarcraft.ai.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.core.utility.KeyValue;

/**
 * SubsetVertex is used to select a subset of the activations out of another
 * GraphVertex.<br>
 * For example, a subset of the activations out of a layer.<br>
 * Note that this subset is specifying by means of an interval of the original
 * activations. For example, to get the first 10 activations of a layer (or,
 * first 10 features out of a CNN layer) use new SubsetVertex(0,9).<br>
 * In the case of convolutional (4d) activations, this is done along depth.
 * 
 * @author Alex Black
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
// TODO 准备改名为HorizontalDetachVertex
public class HorizontalUnstackVertex extends AbstractVertex {

	// inclusive
	private int from;

	// exclusive
	private int to;

	protected HorizontalUnstackVertex() {
	}

	public HorizontalUnstackVertex(String name, MatrixFactory factory, int from, int to) {
		super(name, factory);
		this.from = from;
		this.to = to;
	}

	@Override
	public void doCache(KeyValue<MathMatrix, MathMatrix>... samples) {
		super.doCache(samples);

		// 获取样本的数量与维度
		MathMatrix outputData = ColumnCompositeMatrix.detachOf(ColumnCompositeMatrix.class.cast(samples[0].getKey()), from, to);
		outputKeyValue.setKey(outputData);
		MathMatrix innerError = ColumnCompositeMatrix.detachOf(ColumnCompositeMatrix.class.cast(samples[0].getValue()), from, to);
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
			HorizontalUnstackVertex that = (HorizontalUnstackVertex) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.vertexName, that.vertexName);
			equal.append(this.from, that.from);
			equal.append(this.vertexName, that.vertexName);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(vertexName);
		hash.append(from);
		hash.append(vertexName);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "SeparateVertex(name=" + vertexName + ", from=" + from + ", to=" + to + ")";
	}

}
