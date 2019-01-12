package com.jstarcraft.ai.neuralnetwork.vertex.operation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.core.utility.KeyValue;

/**
 * Multiply节点
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class MultiplyVertex extends AbstractVertex {

	protected MultiplyVertex() {
	}

	public MultiplyVertex(String name, MathCache factory) {
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

		// 检查样本的维度是否一样
		int columnSize = samples[0].getKey().getColumnSize();
		for (int position = 1; position < samples.length; position++) {
			if (columnSize != samples[position].getKey().getColumnSize()) {
				throw new IllegalArgumentException();
			}
		}

		// TODO 考虑支持CompositeMatrix.
		MathMatrix outputData = factory.makeMatrix(rowSize, columnSize);
		outputKeyValue.setKey(outputData);
		MathMatrix innerError = factory.makeMatrix(rowSize, columnSize);
		outputKeyValue.setValue(innerError);
	}

	@Override
	public void doForward() {
		MathMatrix outputData = outputKeyValue.getKey();
		outputData.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = 1F;
			for (KeyValue<MathMatrix, MathMatrix> keyValue : inputKeyValues) {
				MathMatrix inputData = keyValue.getKey();
				value *= inputData.getValue(row, column);
			}
			scalar.setValue(value);
		});
		MathMatrix innerError = outputKeyValue.getValue();
		innerError.setValues(0F);
	}

	@Override
	public void doBackward() {
		MathMatrix innerError = outputKeyValue.getValue();
		int size = inputKeyValues.length;
		innerError.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = scalar.getValue();
			for (int thisIndex = 0; thisIndex < size; thisIndex++) {
				KeyValue<MathMatrix, MathMatrix> keyValue = inputKeyValues[thisIndex];
				MathMatrix outerError = keyValue.getValue();
				if (outerError != null) {
					float error = value;
					for (int thatIndex = 0; thatIndex < size; thatIndex++) {
						if (thisIndex != thatIndex) {
							error *= inputKeyValues[thatIndex].getKey().getValue(row, column);
						}
					}
					// TODO 使用累计的方式计算
					// TODO 需要锁机制,否则并发计算会导致Bug
					outerError.shiftValue(row, column, error);
				}
			}
			return;
		});
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
			MultiplyVertex that = (MultiplyVertex) object;
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
		return "MultiplyVertex(name=" + vertexName + ")";
	}

}
