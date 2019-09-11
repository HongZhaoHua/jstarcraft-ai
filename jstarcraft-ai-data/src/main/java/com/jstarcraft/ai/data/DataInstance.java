package com.jstarcraft.ai.data;

/**
 * 数据实例
 * 
 * @author Birdy
 *
 */
public interface DataInstance {

    public final static int defaultInteger = -1;

    public final static float defaultFloat = Float.NaN;

    public final static float defaultWeight = Float.NaN;

    /**
     * 设置游标
     * 
     * @param cursor
     */
    void setCursor(int cursor);

    /**
     * 获取游标
     * 
     * @return
     */
    int getCursor();

    /**
     * 获取指定维度的离散特征
     * 
     * @param dimension
     * @return
     */
    int getQualityFeature(int dimension);

    /**
     * 获取指定维度的连续特征
     * 
     * @param dimension
     * @return
     */
    float getQuantityFeature(int dimension);

    /**
     * 遍历离散特征
     * 
     * @param accessor
     * @return
     */
    DataInstance iterateQualityFeatures(QualityAccessor accessor);

    /**
     * 遍历连续特征
     * 
     * @param accessor
     * @return
     */
    DataInstance iterateQuantityFeatures(QuantityAccessor accessor);

    /**
     * 获取离散秩
     * 
     * @return
     */
    int getQualityOrder();

    /**
     * 获取连续秩
     * 
     * @return
     */
    int getQuantityOrder();

    /**
     * 获取离散标记
     * 
     * @return
     */
    int getQualityMark();

    /**
     * 获取连续标记
     * 
     * @return
     */
    float getQuantityMark();

    /**
     * 获取权重
     * 
     * @return
     */
    float getWeight();

    /**
     * 设置离散标记
     * 
     * @param mark
     */
    void setQualityMark(int mark);

    /**
     * 设置连续标记
     * 
     * @param mark
     */
    void setQuantityMark(float mark);

    /**
     * 设置权重
     * 
     * @param weight
     */
    void setWeight(float weight);

}
