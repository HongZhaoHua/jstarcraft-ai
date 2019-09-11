package com.jstarcraft.ai.model.neuralnetwork.vertex;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;

import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.GlobalMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.Nd4jMatrix;
import com.jstarcraft.ai.model.neuralnetwork.vertex.transformation.HorizontalAttachVertex;
import com.jstarcraft.ai.modem.ModemDefinition;
import com.jstarcraft.core.utility.KeyValue;

/**
 * Nd4j节点
 * 
 * @author Birdy
 *
 */
@ModemDefinition(value = { "vertexName", "factory", "orientation" })
// TODO 准备整合到StackVertex
@Deprecated
public class Nd4jVertex extends AbstractVertex {

    protected boolean orientation;

    protected Nd4jVertex() {
    }

    public Nd4jVertex(String name, MathCache factory, boolean orientation) {
        super(name, factory);
        this.orientation = orientation;
    }

    @Override
    public void doCache(KeyValue<MathMatrix, MathMatrix>... samples) {
        super.doCache(samples);

        // 检查样本
        if (samples.length != 1) {
            throw new IllegalArgumentException();
        }

        GlobalMatrix keyMatrix = GlobalMatrix.class.cast(inputKeyValues[0].getKey());
        MathMatrix outputData = new Nd4jMatrix(Nd4j.zeros(keyMatrix.getRowSize(), keyMatrix.getColumnSize()));
        outputKeyValue.setKey(outputData);

        GlobalMatrix valueMatrix = GlobalMatrix.class.cast(inputKeyValues[0].getValue());
        MathMatrix innerError = new Nd4jMatrix(Nd4j.zeros(valueMatrix.getRowSize(), valueMatrix.getColumnSize()));
        outputKeyValue.setValue(innerError);
    }

    @Override
    public void doForward() {
        GlobalMatrix inputKeyMatrix = GlobalMatrix.class.cast(inputKeyValues[0].getKey());
        GlobalMatrix inputValueMatrix = GlobalMatrix.class.cast(inputKeyValues[0].getValue());
        Nd4jMatrix outputKeyMatrix = Nd4jMatrix.class.cast(outputKeyValue.getKey());
        Nd4jMatrix outputValueMatrix = Nd4jMatrix.class.cast(outputKeyValue.getValue());

        {
            INDArray outputData = outputKeyMatrix.getArray();
            int cursor = 0;
            for (MathMatrix component : inputKeyMatrix.getComponentMatrixes()) {
                Nd4jMatrix nd4j = Nd4jMatrix.class.cast(component);
                INDArray array = nd4j.getArray();
                if (orientation) {
                    outputData.put(new INDArrayIndex[] { NDArrayIndex.all(), NDArrayIndex.interval(cursor, cursor + array.columns()) }, array);
                    cursor += array.columns();
                } else {
                    outputData.put(new INDArrayIndex[] { NDArrayIndex.interval(cursor, cursor + array.rows()), NDArrayIndex.all() }, array);
                    cursor += array.rows();
                }
            }
        }

        outputValueMatrix.setValues(0F);
    }

    @Override
    public void doBackward() {
        GlobalMatrix inputKeyMatrix = GlobalMatrix.class.cast(inputKeyValues[0].getKey());
        GlobalMatrix inputValueMatrix = GlobalMatrix.class.cast(inputKeyValues[0].getValue());
        Nd4jMatrix outputKeyMatrix = Nd4jMatrix.class.cast(outputKeyValue.getKey());
        Nd4jMatrix outputValueMatrix = Nd4jMatrix.class.cast(outputKeyValue.getValue());

        {
            INDArray innerError = outputValueMatrix.getArray();
            int cursor = 0;
            for (MathMatrix component : inputValueMatrix.getComponentMatrixes()) {
                // TODO 使用累计的方式计算
                // TODO 需要锁机制,否则并发计算会导致Bug
                Nd4jMatrix nd4j = Nd4jMatrix.class.cast(component);
                INDArray array = nd4j.getArray();
                synchronized (component) {
                    if (orientation) {
                        array.addi(innerError.get(new INDArrayIndex[] { NDArrayIndex.all(), NDArrayIndex.interval(cursor, cursor + array.columns()) }));
                        cursor += array.columns();
                    } else {
                        array.addi(innerError.get(new INDArrayIndex[] { NDArrayIndex.interval(cursor, cursor + array.rows()), NDArrayIndex.all() }));
                        cursor += array.rows();
                    }
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
            HorizontalAttachVertex that = (HorizontalAttachVertex) object;
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
        return "Nd4jVertex(name=" + vertexName + ")";
    }

}
