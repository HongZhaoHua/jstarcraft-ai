package com.jstarcraft.ai.neuralnetwork.vertex.operation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.ModelDefinition;
import com.jstarcraft.ai.neuralnetwork.MatrixFactory;
import com.jstarcraft.ai.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.core.utility.KeyValue;

/**
 * A ShiftVertex is used to shift the activations of a single layer<br>
 * One could use it to add a bias or as part of some other calculation. For
 * example, Highway Layers need them in two places. One, it's often useful to
 * have the gate weights have a large negative bias. (Of course for this, we
 * could just initialize the biases that way.) But, _also_ it needs to do this:
 * (1-sigmoid(weight * input + bias)) (*) input + sigmoid(weight * input + bias)
 * (*) activation(w2 * input + bias) ((*) is hadamard product) So, here, we
 * could have 1. a DenseLayer that does the sigmoid 2. a ScaleVertex(-1) and 3.
 * a ShiftVertex(1) to accomplish that.
 *
 * @author Binesh Bannerjee (binesh_binesh@hotmail.com, @bnsh on gitter)
 */
/**
 * Euclidean节点
 * 
 * <pre></pre>
 * 
 * @author Birdy
 *
 */
@ModelDefinition(value = { "vertexName", "factory", "shiftFactor" })
public class ShiftVertex extends AbstractVertex {

	private float shiftFactor;

	protected ShiftVertex() {
	}

	public ShiftVertex(String name, MatrixFactory factory, float shiftFactor) {
		super(name, factory);
		this.shiftFactor = shiftFactor;
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
		MathMatrix outputData = factory.makeCache(rowSize, columnSize);
		outputKeyValue.setKey(outputData);
		MathMatrix innerError = factory.makeCache(rowSize, columnSize);
		outputKeyValue.setValue(innerError);
	}

	@Override
	public void doForward() {
		MathMatrix inputData = inputKeyValues[0].getKey();
		MathMatrix outputData = outputKeyValue.getKey();
		outputData.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
			int row = scalar.getRow();
			int column = scalar.getColumn();
			float value = inputData.getValue(row, column) + shiftFactor;
			scalar.setValue(value);
		});
		MathMatrix innerError = outputKeyValue.getValue();
		innerError.setValues(0F);
	}

	@Override
	public void doBackward() {
		MathMatrix innerError = outputKeyValue.getValue();
		MathMatrix outerError = inputKeyValues[0].getValue();
		if (outerError != null) {
			outerError.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				// TODO 使用累计的方式计算
				// TODO 需要锁机制,否则并发计算会导致Bug
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = scalar.getValue();
				value = value + innerError.getValue(row, column);
				scalar.setValue(value);
			});
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
			ShiftVertex that = (ShiftVertex) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.vertexName, that.vertexName);
			equal.append(this.shiftFactor, that.shiftFactor);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(vertexName);
		hash.append(shiftFactor);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "ShiftVertex(name=" + vertexName + ", shift=" + shiftFactor + ")";
	}

}
