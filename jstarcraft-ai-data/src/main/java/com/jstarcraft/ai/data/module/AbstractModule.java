package com.jstarcraft.ai.data.module;

import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.utility.FloatArray;
import com.jstarcraft.ai.utility.IntegerArray;
import com.jstarcraft.core.utility.KeyValue;

/**
 * 抽象模块
 * 
 * @author Birdy
 *
 */
abstract public class AbstractModule implements DataModule {

    /** 离散特征 */
    protected int qualityOrder;

    /** 连续特征 */
    protected int quantityOrder;

    /** 从外部索引到属性的投影(true代表离散,false代表连续) */
    protected TreeMap<Integer, KeyValue<String, Boolean>> outer = new TreeMap<>();

    /** 从离散属性到内部索引的投影 */
    protected TreeMap<String, Integer> qualityInner = new TreeMap<>();

    /** 从连续属性到内部索引的投影 */
    protected TreeMap<String, Integer> quantityInner = new TreeMap<>();

    /** 离散标记 */
    protected IntegerArray qualityMarks;

    /** 连续标记 */
    protected FloatArray quantityMarks;

    /** 权重 */
    protected FloatArray weights;

    protected int capacity;

    protected int size;

    protected AbstractModule(String name, List<KeyValue<KeyValue<String, Boolean>, Integer>> definition, int capacity) {
        for (KeyValue<KeyValue<String, Boolean>, Integer> term : definition) {
            KeyValue<String, Boolean> keyValue = term.getKey();
            if (keyValue.getValue()) {
                this.outer.put(this.qualityOrder + this.quantityOrder, keyValue);
                this.qualityInner.put(keyValue.getKey(), this.qualityInner.getOrDefault(keyValue.getKey(), 0) + term.getValue());
                this.qualityOrder += term.getValue();
            } else {
                this.outer.put(this.qualityOrder + this.quantityOrder, keyValue);
                this.quantityInner.put(keyValue.getKey(), this.quantityInner.getOrDefault(keyValue.getKey(), 0) + term.getValue());
                this.quantityOrder += term.getValue();
            }
        }
        {
            int count = 0;
            for (Entry<String, Integer> term : qualityInner.entrySet()) {
                int size = term.getValue();
                term.setValue(count);
                count += size;
            }
        }
        {
            int count = 0;
            for (Entry<String, Integer> term : quantityInner.entrySet()) {
                int size = term.getValue();
                term.setValue(count);
                count += size;
            }
        }
        this.qualityMarks = new IntegerArray(1000, capacity);
        this.quantityMarks = new FloatArray(1000, capacity);
        this.weights = new FloatArray(1000, capacity);
        this.capacity = capacity;
    }

    @Override
    public Entry<Integer, KeyValue<String, Boolean>> getOuterKeyValue(int index) {
        // 通过小于等于查找
        return outer.floorEntry(index);
    }

    @Override
    public int getQualityInner(String name) {
        if (name == null) {
            return -1;
        }
        // 通过等于查找
        return qualityInner.getOrDefault(name, -1);
    }

    @Override
    public int getQuantityInner(String name) {
        if (name == null) {
            return -1;
        }
        // 通过等于查找
        return quantityInner.getOrDefault(name, -1);
    }

    @Override
    public int getQualityOrder() {
        return qualityOrder;
    }

    @Override
    public int getQuantityOrder() {
        return quantityOrder;
    }

    IntegerArray getQualityMarks() {
        return qualityMarks;
    }

    FloatArray getQuantityMarks() {
        return quantityMarks;
    }

    FloatArray getWeights() {
        return weights;
    }

}
