package com.jstarcraft.ai.utility;

import java.util.ArrayList;

/**
 * 整型数组
 * 
 * @author Birdy
 *
 */
public class IntegerArray implements CapacityArray {

    /** 最大容量 */
    private int maximumCapacity;

    /** 最小容量 */
    private int minimumCapacity;

    private int[] current;

    private ArrayList<int[]> datas;

    /** 大小 */
    private int size;

    public IntegerArray() {
        this(1000, 1000 * 1000 * 1000);
    }

    public IntegerArray(int minimumCapacity, int maximumCapacity) {
        this.minimumCapacity = minimumCapacity;
        this.maximumCapacity = maximumCapacity;
        this.datas = new ArrayList<>(maximumCapacity / minimumCapacity + (maximumCapacity % minimumCapacity == 0 ? 0 : 1));
        this.size = 0;
    }

    public void associateData(int data) {
        int position = size++ % minimumCapacity;
        if (position == 0) {
            if (size >= maximumCapacity) {
                current = null;
                throw new IllegalStateException();
            } else {
                current = new int[minimumCapacity];
                datas.add(current);
            }
        }
        current[position] = data;
    }

    public int getData(int cursor) {
        return datas.get(cursor / minimumCapacity)[cursor % minimumCapacity];
    }

    public void setData(int cursor, int data) {
        datas.get(cursor / minimumCapacity)[cursor % minimumCapacity] = data;
    }

    @Override
    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    @Override
    public int getMinimumCapacity() {
        return minimumCapacity;
    }

    @Override
    public int getSize() {
        return size;
    }

}
