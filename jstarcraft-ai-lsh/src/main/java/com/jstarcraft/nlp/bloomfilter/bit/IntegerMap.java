package com.jstarcraft.nlp.bloomfilter.bit;

/**
 * 
 * @author Birdy
 *
 */
public class IntegerMap implements BitMap {

    private int[] bits;

    private int capacity;

    private int size;

    public IntegerMap(int capacity) {
        assert capacity > 0;
        int elements = capacity % Integer.SIZE == 0 ? capacity / Integer.SIZE : capacity / Integer.SIZE + 1;
        this.bits = new int[elements];
        this.capacity = capacity;
        this.size = 0;
    }

    @Override
    public boolean get(int index) {
        int row = index / Integer.SIZE;
        int column = index % Integer.SIZE;
        return ((bits[row] >>> column) & 1) == 1;
    }

    @Override
    public void set(int index) {
        int row = index / Integer.SIZE;
        int column = index % Integer.SIZE;
        if (((bits[row] >>> column) & 1) == 0) {
            bits[row] |= (1 << column);
            size++;
        }
    }

    @Override
    public void unset(int index) {
        int row = index / Integer.SIZE;
        int column = index % Integer.SIZE;
        if (((bits[row] >>> column) & 1) == 1) {
            bits[row] &= ~(1 << column);
            size--;
        }
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public int size() {
        return size;
    }

}