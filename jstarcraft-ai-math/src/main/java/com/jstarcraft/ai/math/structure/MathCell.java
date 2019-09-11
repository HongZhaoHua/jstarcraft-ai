package com.jstarcraft.ai.math.structure;

/**
 * 数学单元
 * 
 * @author Birdy
 *
 * @param <T>
 */
public interface MathCell<T> {

    /**
     * 获取单元所在行的索引
     * 
     * @return
     */
    int getRow();

    /**
     * 获取单元所在列的索引
     * 
     * @return
     */
    int getColumn();

    /**
     * 获取单元的值
     * 
     * @return
     */
    T getValue();

    /**
     * 设置单元的值
     * 
     * @param value
     */
    void setValue(T value);

}
