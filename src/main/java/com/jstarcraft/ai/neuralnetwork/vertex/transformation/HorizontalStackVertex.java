package com.jstarcraft.ai.neuralnetwork.vertex.transformation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.matrix.ColumnCompositeMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.neuralnetwork.MatrixFactory;
import com.jstarcraft.ai.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.core.utility.KeyValue;

/**
 * A MergeVertex is used to combine the activations of two or more
 * layers/GraphVertex by means of concatenation/merging.<br>
 * Exactly how this is done depends on the type of input.<br>
 * For 2d (feed forward layer) inputs:
 * MergeVertex([numExamples,layerSize1],[numExamples,layerSize2]) ->
 * [numExamples,layerSize1 + layerSize2]<br>
 * For 3d (time series) inputs:
 * MergeVertex([numExamples,layerSize1,timeSeriesLength],[numExamples,layerSize2,timeSeriesLength])
 * -> [numExamples,layerSize1 + layerSize2,timeSeriesLength]<br>
 * For 4d (convolutional) inputs:
 * MergeVertex([numExamples,depth1,width,height],[numExamples,depth2,width,height])
 * -> [numExamples,depth1 + depth2,width,height]<br>
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
// TODO 准备改名为HorizontalAttachVertex
public class HorizontalStackVertex extends AbstractVertex {

	protected HorizontalStackVertex() {
	}

	public HorizontalStackVertex(String name, MatrixFactory factory) {
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

		MathMatrix outputData = ColumnCompositeMatrix.attachOf(keys);
		outputKeyValue.setKey(outputData);
		MathMatrix innerError = ColumnCompositeMatrix.attachOf(values);
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
			HorizontalStackVertex that = (HorizontalStackVertex) object;
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
