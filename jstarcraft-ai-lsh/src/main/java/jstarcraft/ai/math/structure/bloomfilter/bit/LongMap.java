package jstarcraft.ai.math.structure.bloomfilter.bit;

public class LongMap implements BitMap {

    private long[] bits;

    private int capacity;

    private int size;

    public LongMap(int capacity) {
        assert capacity > 0;
        int elements = capacity % Long.SIZE == 0 ? capacity / Long.SIZE : capacity / Long.SIZE + 1;
        this.bits = new long[elements];
        this.capacity = capacity;
        this.size = 0;
    }

    @Override
    public boolean get(int index) {
        int row = index / Long.SIZE;
        int column = index % Long.SIZE;
        return ((bits[row] >>> column) & 1L) == 1L;
    }

    @Override
    public void set(int index) {
        int row = index / Long.SIZE;
        int column = index % Long.SIZE;
        bits[row] |= (1L << column);
        size++;
    }

    @Override
    public void unset(int index) {
        int row = index / Long.SIZE;
        int column = index % Long.SIZE;
        bits[row] &= ~(1L << column);
        size--;
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