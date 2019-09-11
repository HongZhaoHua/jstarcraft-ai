package com.jstarcraft.ai.math.structure;

/**
 * 单元迭代器
 * 
 * @author Birdy
 *
 */
public interface CellIterator<T> extends MathIterator<MathCell<T>> {

    /**
     * 设置所有单元的值
     * 
     * @param value
     * @return
     */
    CellIterator<T> setValues(T value);

}
