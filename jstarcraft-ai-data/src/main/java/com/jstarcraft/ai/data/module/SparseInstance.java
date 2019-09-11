package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.QuantityAccessor;
import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.FloatArray;
import com.jstarcraft.ai.data.IntegerArray;
import com.jstarcraft.ai.data.QualityAccessor;

/**
 * 稀疏实例
 * 
 * @author Birdy
 *
 */
public class SparseInstance implements DataInstance {

    /** 游标 */
    private int cursor;

    /** 离散秩 */
    private int qualityOrder;

    /** 连续秩 */
    private int quantityOrder;

    /** 离散特征 */
    private int[] qualityFeatures;

    private IntegerArray qualityPoints;

    private IntegerArray qualityDimensions;

    private IntegerArray qualityValues;

    /** 连续特征 */
    private float[] quantityFeatures;

    private IntegerArray quantityPoints;

    private IntegerArray quantityDimensions;

    private FloatArray quantityValues;

    /** 离散标记 */
    protected IntegerArray qualityMarks;

    /** 连续标记 */
    protected FloatArray quantityMarks;

    /** 权重 */
    private FloatArray weights;

    SparseInstance(int cursor, SparseModule module) {
        this.cursor = cursor;
        this.qualityOrder = module.getQualityOrder();
        this.quantityOrder = module.getQuantityOrder();
        this.qualityFeatures = new int[module.getQualityOrder()];
        {
            for (int index = 0, size = module.getQualityOrder(); index < size; index++) {
                this.qualityFeatures[index] = DataInstance.defaultInteger;
            }
        }
        this.qualityPoints = module.getQualityPoints();
        this.qualityDimensions = module.getQualityIndexes();
        this.qualityValues = module.getQualityValues();
        this.quantityFeatures = new float[module.getQuantityOrder()];
        {
            for (int index = 0, size = module.getQuantityOrder(); index < size; index++) {
                this.quantityFeatures[index] = DataInstance.defaultFloat;
            }
        }
        this.quantityPoints = module.getquantityPoints();
        this.quantityDimensions = module.getquantityIndexes();
        this.quantityValues = module.getquantityValues();
        {
            int from = this.qualityPoints.getData(this.cursor);
            int to = this.qualityPoints.getData(this.cursor + 1);
            for (int position = from; position < to; position++) {
                int index = this.qualityDimensions.getData(position);
                this.qualityFeatures[index] = this.qualityValues.getData(position);
            }
        }
        {
            int from = this.quantityPoints.getData(this.cursor);
            int to = this.quantityPoints.getData(this.cursor + 1);
            for (int position = from; position < to; position++) {
                int index = this.quantityDimensions.getData(position);
                this.quantityFeatures[index] = this.quantityValues.getData(position);
            }
        }
        this.qualityMarks = module.getQualityMarks();
        this.quantityMarks = module.getQuantityMarks();
        this.weights = module.getWeights();
    }

    @Override
    public void setCursor(int cursor) {
        {
            int from = this.qualityPoints.getData(this.cursor);
            int to = this.qualityPoints.getData(this.cursor + 1);
            for (int current = from; current < to; current++) {
                int dimension = this.qualityDimensions.getData(current);
                this.qualityFeatures[dimension] = DataInstance.defaultInteger;
            }
        }
        {
            int from = this.quantityPoints.getData(this.cursor);
            int to = this.quantityPoints.getData(this.cursor + 1);
            for (int current = from; current < to; current++) {
                int dimension = this.quantityDimensions.getData(current);
                this.quantityFeatures[dimension] = DataInstance.defaultFloat;
            }
        }
        this.cursor = cursor;
        {
            int from = this.qualityPoints.getData(this.cursor);
            int to = this.qualityPoints.getData(this.cursor + 1);
            for (int current = from; current < to; current++) {
                int dimension = this.qualityDimensions.getData(current);
                this.qualityFeatures[dimension] = this.qualityValues.getData(current);
            }
        }
        {
            int from = this.quantityPoints.getData(this.cursor);
            int to = this.quantityPoints.getData(this.cursor + 1);
            for (int current = from; current < to; current++) {
                int dimension = this.quantityDimensions.getData(current);
                this.quantityFeatures[dimension] = this.quantityValues.getData(current);
            }
        }
    }

    @Override
    public int getCursor() {
        return cursor;
    }

    @Override
    public int getQualityFeature(int dimension) {
        return this.qualityFeatures[dimension];
    }

    @Override
    public float getQuantityFeature(int dimension) {
        return this.quantityFeatures[dimension];
    }

    @Override
    public SparseInstance iterateQualityFeatures(QualityAccessor accessor) {
        int from = this.qualityPoints.getData(this.cursor);
        int to = this.qualityPoints.getData(this.cursor + 1);
        for (int position = from; position < to; position++) {
            int dimension = this.qualityDimensions.getData(position);
            accessor.accessorFeature(dimension, this.qualityFeatures[dimension]);
        }
        return this;
    }

    @Override
    public SparseInstance iterateQuantityFeatures(QuantityAccessor accessor) {
        int from = this.quantityPoints.getData(this.cursor);
        int to = this.quantityPoints.getData(this.cursor + 1);
        for (int position = from; position < to; position++) {
            int dimension = this.quantityDimensions.getData(position);
            accessor.accessorFeature(dimension, this.quantityFeatures[dimension]);
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
        return qualityMarks.getData(cursor);
    }

    @Override
    public float getQuantityMark() {
        return quantityMarks.getData(cursor);
    }

    @Override
    public float getWeight() {
        return weights.getData(cursor);
    }

    @Override
    public void setQualityMark(int mark) {
        qualityMarks.setData(cursor, mark);
    }

    @Override
    public void setQuantityMark(float mark) {
        quantityMarks.setData(cursor, mark);
    }

    @Override
    public void setWeight(float weight) {
        weights.setData(cursor, weight);
    }

}
