package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.QualityAccessor;
import com.jstarcraft.ai.data.QuantityAccessor;

/**
 * 数组实例
 * 
 * @author Birdy
 *
 */
public class ArrayInstance implements DataInstance {

    /** 离散秩 */
    private int qualityOrder;

    /** 连续秩 */
    private int quantityOrder;

    /** 离散特征 */
    private int[] qualityFeatures;

    /** 连续特征 */
    private float[] quantityFeatures;

    /** 离散标记 */
    private int qualityMark;

    /** 连续标记 */
    private float quantityMark;

    /** 权重 */
    private float weight;

    public ArrayInstance(int qualityOrder, int quantityOrder) {
        this.qualityOrder = qualityOrder;
        this.quantityOrder = quantityOrder;
        this.qualityFeatures = new int[qualityOrder];
        this.quantityFeatures = new float[quantityOrder];
        for (int index = 0; index < qualityOrder; index++) {
            this.qualityFeatures[index] = DataInstance.defaultInteger;
        }
        for (int index = 0; index < quantityOrder; index++) {
            this.quantityFeatures[index] = DataInstance.defaultFloat;
        }
        this.qualityMark = defaultInteger;
        this.quantityMark = defaultFloat;
        this.weight = DataInstance.defaultWeight;
    }

    @Override
    public void setCursor(int cursor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCursor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getQualityFeature(int index) {
        return this.qualityFeatures[index];
    }

    @Override
    public float getQuantityFeature(int index) {
        return this.quantityFeatures[index];
    }

    @Override
    public ArrayInstance iterateQualityFeatures(QualityAccessor accessor) {
        for (int index = 0; index < qualityOrder; index++) {
            accessor.accessorFeature(index, this.qualityFeatures[index]);
        }
        return this;
    }

    @Override
    public ArrayInstance iterateQuantityFeatures(QuantityAccessor accessor) {
        for (int index = 0; index < quantityOrder; index++) {
            accessor.accessorFeature(index, this.quantityFeatures[index]);
        }
        return this;
    }

    @Override
    public int getQualityOrder() {
        return qualityOrder;
    }

    @Override
    public int getQuantityOrder() {
        return quantityOrder;
    }

    @Override
    public int getQualityMark() {
        return qualityMark;
    }

    @Override
    public float getQuantityMark() {
        return quantityMark;
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    public void setQualityMark(int qualityMark) {
        this.qualityMark = qualityMark;
    }

    @Override
    public void setQuantityMark(float quantityMark) {
        this.quantityMark = quantityMark;
    }

    @Override
    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setQualityFeature(int index, int value) {
        this.qualityFeatures[index] = value;
    }

    public void setQuantityFeature(int index, float value) {
        this.quantityFeatures[index] = value;
    }

    public void copyInstance(DataInstance instance) {
        for (int index = 0; index < qualityOrder; index++) {
            this.qualityFeatures[index] = DataInstance.defaultInteger;
        }
        for (int index = 0; index < quantityOrder; index++) {
            this.quantityFeatures[index] = DataInstance.defaultFloat;
        }
        instance.iterateQualityFeatures((index, value) -> {
            this.qualityFeatures[index] = value;
        });
        instance.iterateQuantityFeatures((index, value) -> {
            this.quantityFeatures[index] = value;
        });
        this.qualityMark = instance.getQualityMark();
        this.quantityMark = instance.getQuantityMark();
        this.weight = instance.getWeight();
    }

}
