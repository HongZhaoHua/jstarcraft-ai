package com.jstarcraft.ai.math.structure;

/**
 * 数学监控器
 * 
 * @author Birdy
 *
 */
public interface MathMonitor<T> {

    /**
     * 通知数量变更
     * 
     * @param iterator
     * @param oldElementSize
     * @param newElementSize
     */
    void notifySizeChanged(MathIterator<T> iterator, int oldElementSize, int newElementSize, int oldKnownSize, int newKnownSize, int oldUnknownSize, int newUnknownSize);

}
