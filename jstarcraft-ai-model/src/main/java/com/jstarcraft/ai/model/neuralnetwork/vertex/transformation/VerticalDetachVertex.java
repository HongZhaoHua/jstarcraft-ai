package com.jstarcraft.ai.model.neuralnetwork.vertex.transformation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.RowGlobalMatrix;
import com.jstarcraft.ai.model.neuralnetwork.vertex.AbstractVertex;
import com.jstarcraft.ai.modem.ModemDefinition;
import com.jstarcraft.core.utility.KeyValue;

/**
 * VerticalDetachVertex节点
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
@ModemDefinition(value = { "vertexName", "factory", "from", "to" })
public class VerticalDetachVertex extends AbstractVertex {

    // inclusive
    private int from;

    // exclusive
    private int to;

    protected VerticalDetachVertex() {
    }

    public VerticalDetachVertex(String name, MathCache factory, int from, int to) {
        super(name, factory);
        this.from = from;
        this.to = to;
    }

    @Override
    public void doCache(KeyValue<MathMatrix, MathMatrix>... samples) {
        super.doCache(samples);

        // 获取样本的数量与维度
        MathMatrix outputData = RowGlobalMatrix.detachOf(RowGlobalMatrix.class.cast(samples[0].getKey()), from, to);
        outputKeyValue.setKey(outputData);
        MathMatrix innerError = RowGlobalMatrix.detachOf(RowGlobalMatrix.class.cast(samples[0].getValue()), from, to);
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
            VerticalDetachVertex that = (VerticalDetachVertex) object;
            EqualsBuilder equal = new EqualsBuilder();
            equal.append(this.vertexName, that.vertexName);
            equal.append(this.from, that.from);
            equal.append(this.to, that.to);
            return equal.isEquals();
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder();
        hash.append(vertexName);
        hash.append(from);
        hash.append(to);
        return hash.toHashCode();
    }

    @Override
    public String toString() {
        return "UnstackVertex(name=" + vertexName + ", from=" + from + ", to=" + to + ")";
    }

}
