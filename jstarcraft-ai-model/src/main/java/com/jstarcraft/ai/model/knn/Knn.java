package com.jstarcraft.ai.model.knn;

import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Knn<T> {

    private int k;

    private Comparator<T> comparator;

    private NavigableSet<T> neighbors;

    private volatile T keyValue;

    public Knn(int k, Comparator<T> comparator) {
        this.k = k;
        this.comparator = comparator;
        this.neighbors = new TreeSet<>(comparator);
    }

    /**
     * 更新邻居
     * 
     * @param key
     * @param value
     */
    public synchronized void updateNeighbor(T keyValue) {
        if (neighbors.size() >= k) {
            // 与边界值比较再判断是否更新
            if (comparator.compare(keyValue, this.keyValue) < 0) {
                neighbors.add(keyValue);
                if (neighbors.size() > k) {
                    // 防止大小不变的情况下
                    neighbors.pollLast();
                }
                this.keyValue = neighbors.last();
            }
        } else {
            neighbors.add(keyValue);
            this.keyValue = neighbors.last();
        }
    }

    /**
     * 获取邻居
     * 
     * @return
     */
    public Collection<T> getNeighbors() {
        return neighbors;
    }

}
