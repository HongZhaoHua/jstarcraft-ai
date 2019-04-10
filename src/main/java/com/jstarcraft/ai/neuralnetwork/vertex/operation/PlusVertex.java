package com.jstarcraft.ai.neuralnetwork.vertex.operation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.ColumnGlobalMatrix;
import com.jstarcraft.ai.math.structure.matrix.GlobalMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.RowGlobalMatrix;
import com.jstarcraft.ai.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.core.utility.KeyValue;

/**
 * Plus节点
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class PlusVertex extends AbstractVertex {

	protected PlusVertex() {
	}

	public PlusVertex(String name, MathCache factory) {
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
		if (samples[0].getKey() instanceof GlobalMatrix) {
			GlobalMatrix matrix = GlobalMatrix.class.cast(samples[0].getKey());
			int size = matrix.getComponentSize();
			MathMatrix[] outputDatas = new MathMatrix[size];
			MathMatrix[] innerErrors = new MathMatrix[size];
			for (int index = 0; index < size; index++) {
				MathMatrix component = matrix.getComponentMatrix(index);
				outputDatas[index] = factory.makeMatrix(component.getRowSize(), component.getColumnSize());
				innerErrors[index] = factory.makeMatrix(component.getRowSize(), component.getColumnSize());
			}
			if (samples[0].getKey() instanceof ColumnGlobalMatrix) {
				MathMatrix outputData = ColumnGlobalMatrix.attachOf(outputDatas);
				outputKeyValue.setKey(outputData);
				MathMatrix innerError = ColumnGlobalMatrix.attachOf(innerErrors);
				outputKeyValue.setValue(innerError);
			}
			if (samples[0].getKey() instanceof RowGlobalMatrix) {
				MathMatrix outputData = RowGlobalMatrix.attachOf(outputDatas);
				outputKeyValue.setKey(outputData);
				MathMatrix innerError = RowGlobalMatrix.attachOf(innerErrors);
				outputKeyValue.setValue(innerError);
			}
		} else {
			MathMatrix outputData = factory.makeMatrix(rowSize, columnSize);
			outputKeyValue.setKey(outputData);
			MathMatrix innerError = factory.makeMatrix(rowSize, columnSize);
			outputKeyValue.setValue(innerError);
		}
	}

	@Override
	public void doForward() {
		MathMatrix outputData = outputKeyValue.getKey();
		outputData.setValues(0F);
		for (KeyValue<MathMatrix, MathMatrix> keyValue : inputKeyValues) {
			MathMatrix inputData = keyValue.getKey();
			outputData.addMatrix(inputData, false);
		}
		MathMatrix innerError = outputKeyValue.getValue();
		innerError.setValues(0F);
	}

	@Override
	public void doBackward() {
		MathMatrix innerError = outputKeyValue.getValue();
		for (KeyValue<MathMatrix, MathMatrix> keyValue : inputKeyValues) {
			MathMatrix outerError = keyValue.getValue();
			if (outerError != null) {
				// TODO 使用累计的方式计算
				// TODO 需要锁机制,否则并发计算会导致Bug
				synchronized (outerError) {
					outerError.addMatrix(innerError, false);
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
			PlusVertex that = (PlusVertex) object;
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
		return "PlusVertex(name=" + vertexName + ")";
	}

}
