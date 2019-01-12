package com.jstarcraft.ai.neuralnetwork.vertex.accumulation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.core.utility.KeyValue;

/**
 * Outer Product节点
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class OuterProductVertex extends AbstractVertex {

	private int rowSize;

	private int columnSize;

	protected OuterProductVertex() {
	}

	public OuterProductVertex(String name, MathCache factory) {
		super(name, factory);
	}

	@Override
	public void doCache(KeyValue<MathMatrix, MathMatrix>... samples) {
		if (samples.length != 2) {
			throw new IllegalArgumentException();
		}

		super.doCache(samples);

		// 检查样本的数量是否一样
		rowSize = samples[0].getKey().getRowSize();
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

		MathMatrix outputData = factory.makeMatrix(rowSize, columnSize * columnSize);
		outputKeyValue.setKey(outputData);
		MathMatrix innerError = factory.makeMatrix(rowSize, columnSize * columnSize);
		outputKeyValue.setValue(innerError);
	}

	@Override
	public void doForward() {
		MathMatrix outputData = outputKeyValue.getKey();
		MathMatrix leftInputData = inputKeyValues[0].getKey();
		MathMatrix rightInputData = inputKeyValues[1].getKey();
		for (int rowIndex = 0, rowSize = outputData.getRowSize(); rowIndex < rowSize; rowIndex++) {
			MathVector leftVector = leftInputData.getRowVector(rowIndex), rightVector = rightInputData.getRowVector(rowIndex);
			outputData.getRowVector(rowIndex).iterateElement(MathCalculator.SERIAL, (scalar) -> {
				int index = scalar.getIndex();
				int leftIndex = index / columnSize;
				int rightIndex = index % columnSize;
				scalar.setValue(leftVector.getValue(leftIndex) * rightVector.getValue(rightIndex));
			});
		}
		MathMatrix innerError = outputKeyValue.getValue();
		innerError.setValues(0F);
	}

	@Override
	public void doBackward() {
		MathMatrix leftInputData = inputKeyValues[0].getKey();
		MathMatrix rightInputData = inputKeyValues[1].getKey();
		MathMatrix innerError = outputKeyValue.getValue();

		MathMatrix leftInputError = inputKeyValues[0].getValue();
		MathMatrix rightInputError = inputKeyValues[1].getValue();
		MathMatrix errorMatrix = factory.makeMatrix(columnSize, columnSize);
		for (int rowIndex = 0, rowSize = innerError.getRowSize(); rowIndex < rowSize; rowIndex++) {
			MathVector errorVector = innerError.getRowVector(rowIndex);
			errorMatrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				scalar.setValue(errorVector.getValue(row * columnSize + column));
			});
			if (leftInputError != null) {
				// TODO 使用累计的方式计算
				// TODO 需要锁机制,否则并发计算会导致Bug
				synchronized (leftInputError) {
					MathVector vector = rightInputData.getRowVector(rowIndex);
					leftInputError.getRowVector(rowIndex).accumulateProduct(errorMatrix, false, vector, MathCalculator.SERIAL);
				}
			}
			if (rightInputError != null) {
				// TODO 使用累计的方式计算
				// TODO 需要锁机制,否则并发计算会导致Bug
				synchronized (rightInputError) {
					MathVector vector = leftInputData.getRowVector(rowIndex);
					rightInputError.getRowVector(rowIndex).accumulateProduct(vector, errorMatrix, false, MathCalculator.SERIAL);
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
			OuterProductVertex that = (OuterProductVertex) object;
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
		return "ProductVertex(name=" + vertexName + ")";
	}

}
