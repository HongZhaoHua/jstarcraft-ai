package com.jstarcraft.ai.math.structure;

public interface MathIterator<T> extends Iterable<T> {

    /**
     * 获取元素(与迭代相关)的大小
     * 
     * @return
     */
    int getElementSize();

    /**
     * 获取已知标量/单元的数量
     * 
     * @return
     */
    int getKnownSize();

    /**
     * 获取未知标量/单元的数量
     * 
     * @return
     */
    int getUnknownSize();

    /**
     * 遍历所有元素
     * 
     * @param mode
     * @param accessors
     * @return
     */
    MathIterator<T> iterateElement(MathCalculator mode, MathAccessor<T>... accessors);

    /**
     * 添加数学监控器
     * 
     * @param monitor
     */
    default void attachMonitor(MathMonitor<T> monitor) {
    }

    /**
     * 删除数学监控器
     * 
     * @param monitor
     */
    default void detachMonitor(MathMonitor<T> monitor) {
    }

}
