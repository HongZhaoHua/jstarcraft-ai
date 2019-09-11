package com.jstarcraft.ai.model.neuralnetwork.vertex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.modem.ModemDefinition;
import com.jstarcraft.core.utility.KeyValue;

/**
 * 抽象节点
 * 
 * @author Birdy
 *
 */
@ModemDefinition(value = { "vertexName", "factory" })
public abstract class AbstractVertex implements Vertex {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected String vertexName;

    protected MathCache factory;

    /** 键为inputData(不可以为null),值为outerError(可以为null) */
    protected KeyValue<MathMatrix, MathMatrix>[] inputKeyValues;

    /** 键为outputData(不可以为null),值为innerError(不可以为null) */
    protected KeyValue<MathMatrix, MathMatrix> outputKeyValue;

    protected AbstractVertex() {
    }

    protected AbstractVertex(String name, MathCache factory) {
        this.vertexName = name;
        this.factory = factory;
    }

    @Override
    public void doCache(KeyValue<MathMatrix, MathMatrix>... samples) {
        // 检查样本
        if (samples.length == 0) {
            throw new IllegalArgumentException();
        }

        this.inputKeyValues = samples;
        this.outputKeyValue = new KeyValue<>(null, null);
    }

    @Override
    public String getVertexName() {
        return vertexName;
    }

    @Override
    public KeyValue<MathMatrix, MathMatrix> getInputKeyValue(int position) {
        return inputKeyValues[position];
    }

    @Override
    public KeyValue<MathMatrix, MathMatrix> getOutputKeyValue() {
        return outputKeyValue;
    }

    @Override
    public abstract String toString();

}
