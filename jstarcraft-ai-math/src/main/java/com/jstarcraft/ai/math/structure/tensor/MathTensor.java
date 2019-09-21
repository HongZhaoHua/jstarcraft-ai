package com.jstarcraft.ai.math.structure.tensor;

import com.jstarcraft.ai.math.structure.ScalarIterator;

public interface MathTensor extends ScalarIterator<TensorScalar> {

    /**
     * 获取形状
     * 
     * @return
     */
    int[] getShape();

    /**
     * 设置形状
     * 
     * @param shape
     */
    void setShape(int... shape);

    /**
     * 获取步幅
     * 
     * @return
     */
    int[] getStride();

    /**
     * 获取秩的大小
     * 
     * @return
     */
    int getOrderSize();

    /**
     * 获取维度的大小
     * 
     * @return
     */
    int getDimensionSize(int dimension);

    /**
     * 是否支持索引访问
     * 
     * <pre>
     * 与getValue,setValue,scaleValue,shiftValue相关
     * </pre>
     * 
     * @return
     */
    boolean isIndexed();

    /**
     * 获取指定索引标量的值
     * 
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    float getValue(int[] indices);

    /**
     * 设置指定索引标量的值
     * 
     * @param rowIndex
     * @param columnIndex
     * @param value
     */
    void setValue(int[] indices, float value);

    /**
     * 缩放指定索引标量的值
     * 
     * @param rowIndex
     * @param columnIndex
     * @param value
     */
    void scaleValue(int[] indices, float value);

    /**
     * 偏移指定索引标量的值
     * 
     * @param rowIndex
     * @param columnIndex
     * @param value
     */
    void shiftValue(int[] indices, float value);

    /**
     * 张量加法运算
     * 
     * @param tensor
     * @return
     */
    default MathTensor addTensor(MathTensor tensor) {
        for (TensorScalar scalar : tensor) {
            int[] indices = scalar.getIndexes();
            shiftValue(indices, scalar.getValue());
        }
        return this;
    }

    /**
     * 张量减法运算
     * 
     * @param tensor
     * @return
     */
    default MathTensor subtractTensor(MathTensor tensor) {
        for (TensorScalar scalar : tensor) {
            int[] indices = scalar.getIndexes();
            shiftValue(indices, -scalar.getValue());
        }
        return this;
    }

    /**
     * 张量乘法运算
     * 
     * @param tensor
     * @return
     */
    default MathTensor multiplyTensor(MathTensor tensor) {
        for (TensorScalar scalar : tensor) {
            int[] indices = scalar.getIndexes();
            scaleValue(indices, scalar.getValue());
        }
        return this;
    }

    /**
     * 张量除法运算
     * 
     * @param tensor
     * @return
     */
    default MathTensor divideTensor(MathTensor tensor) {
        for (TensorScalar scalar : tensor) {
            int[] indices = scalar.getIndexes();
            scaleValue(indices, 1F / scalar.getValue());
        }
        return this;
    }

    /**
     * 张量拷贝运算
     * 
     * @param tensor
     * @return
     */
    default MathTensor copyTensor(MathTensor tensor) {
        for (TensorScalar scalar : tensor) {
            int[] indices = scalar.getIndexes();
            setValue(indices, scalar.getValue());
        }
        return this;
    }

}
