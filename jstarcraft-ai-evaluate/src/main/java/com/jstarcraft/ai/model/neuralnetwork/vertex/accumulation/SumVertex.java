package com.jstarcraft.ai.model.neuralnetwork.vertex.accumulation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.model.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.core.utility.KeyValue;

/**
 * Sum节点
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class SumVertex extends AbstractVertex {

	protected SumVertex() {
	}

	public SumVertex(String name, MathCache factory) {
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

		MathMatrix outputData = factory.makeMatrix(rowSize, 1);
		outputKeyValue.setKey(outputData);
		MathMatrix innerError = factory.makeMatrix(rowSize, 1);
		outputKeyValue.setValue(innerError);
	}

	@Override
	public void doForward() {
		MathMatrix outputData = outputKeyValue.getKey();
		MathVector outputVector = outputData.getColumnVector(0);
		outputVector.setValues(0F);
		for (int index = 0; index < inputKeyValues.length; index++) {
			MathMatrix inputData = inputKeyValues[index].getKey();
			for (int columnIndex = 0, columnSize = inputData.getColumnSize(); columnIndex < columnSize; columnIndex++) {
				MathVector inputVector = inputData.getColumnVector(columnIndex);
				outputVector.addVector(inputVector);
			}
		}
		MathMatrix innerError = outputKeyValue.getValue();
		innerError.setValues(0F);
	}

	@Override
	public void doBackward() {
		MathVector outputData = outputKeyValue.getKey().getColumnVector(0);
		MathVector innerError = outputKeyValue.getValue().getColumnVector(0);

		for (int index = 0; index < inputKeyValues.length; index++) {
			MathMatrix outerError = inputKeyValues[index].getValue();
			if (outerError != null) {
				MathMatrix inputData = inputKeyValues[index].getKey();
				// TODO 使用累计的方式计算
				// TODO 需要锁机制,否则并发计算会导致Bug
				outerError.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
					int row = scalar.getRow();
					int column = scalar.getColumn();
					float value = scalar.getValue();
					value += inputData.getValue(row, column) * innerError.getValue(row) / outputData.getValue(row);
					scalar.setValue(value);
				});
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
			SumVertex that = (SumVertex) object;
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
		return "SumVertex(name=" + vertexName + ")";
	}

}
