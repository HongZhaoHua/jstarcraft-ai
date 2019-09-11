package com.jstarcraft.ai.model.neuralnetwork.vertex.operation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.ai.modem.ModemDefinition;
import com.jstarcraft.core.utility.KeyValue;

/**
 * Shift节点
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
@ModemDefinition(value = { "vertexName", "factory", "shiftFactor" })
public class ShiftVertex extends AbstractVertex {

    private float shiftFactor;

    protected ShiftVertex() {
    }

    public ShiftVertex(String name, MathCache factory, float shiftFactor) {
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
        MathMatrix outputData = factory.makeMatrix(rowSize, columnSize);
        outputKeyValue.setKey(outputData);
        MathMatrix innerError = factory.makeMatrix(rowSize, columnSize);
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
