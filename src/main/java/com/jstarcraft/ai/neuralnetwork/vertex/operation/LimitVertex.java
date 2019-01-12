package com.jstarcraft.ai.neuralnetwork.vertex.operation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.ModelDefinition;
import com.jstarcraft.ai.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.core.utility.KeyValue;

/**
 * Limit节点
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
@ModelDefinition(value = { "vertexName", "factory", "mode" })
public class LimitVertex extends AbstractVertex {

	public enum Mode {
		Maximum, Minimum;
	}

	private Mode mode;

	/** 极限值的索引 */
	private MathMatrix limitIndexes;

	protected LimitVertex() {
	}

	public LimitVertex(String name, MathCache factory, Mode mode) {
		super(name, factory);
		this.mode = mode;
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

		limitIndexes = factory.makeMatrix(rowSize, 1);
	}

	@Override
	public void doForward() {
		MathMatrix outputData = outputKeyValue.getKey();
		switch (mode) {
		case Maximum:
			outputData.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int limit = 0;
				float data;
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = Float.NEGATIVE_INFINITY;
				for (int index = 0, size = inputKeyValues.length; index < size; index++) {
					KeyValue<MathMatrix, MathMatrix> keyValue = inputKeyValues[index];
					MathMatrix inputData = keyValue.getKey();
					data = inputData.getValue(row, column);
					if (value < data) {
						value = data;
						limit = index;
					}
				}
				limitIndexes.setValue(row, column, limit);
				scalar.setValue(value);
			});
			break;
		case Minimum:
			outputData.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int limit = 0;
				float data;
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = Float.POSITIVE_INFINITY;
				for (int index = 0, size = inputKeyValues.length; index < size; index++) {
					KeyValue<MathMatrix, MathMatrix> keyValue = inputKeyValues[index];
					MathMatrix inputData = keyValue.getKey();
					data = inputData.getValue(row, column);
					if (value > data) {
						value = data;
						limit = index;
					}
				}
				limitIndexes.setValue(row, column, limit);
				scalar.setValue(value);
			});
			break;
		}
		MathMatrix innerError = outputKeyValue.getValue();
		innerError.setValues(0F);
	}

	@Override
	public void doBackward() {
		MathMatrix innerError = outputKeyValue.getValue();
		innerError.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = scalar.getValue();
			int index = (int) limitIndexes.getValue(row, column);
			KeyValue<MathMatrix, MathMatrix> keyValue = inputKeyValues[index];
			MathMatrix outerError = keyValue.getValue();
			if (outerError != null) {
				// TODO 使用累计的方式计算
				// TODO 需要锁机制,否则并发计算会导致Bug
				outerError.shiftValue(row, column, value);
			}
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
			LimitVertex that = (LimitVertex) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.vertexName, that.vertexName);
			equal.append(this.mode, that.mode);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(vertexName);
		hash.append(mode);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "LimitVertex(name=" + vertexName + ", mode=" + mode + ")";
	}

}
