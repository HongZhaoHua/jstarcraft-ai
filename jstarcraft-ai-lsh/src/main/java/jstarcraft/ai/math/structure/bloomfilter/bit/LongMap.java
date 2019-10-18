package jstarcraft.ai.math.structure.bloomfilter.bit;

public class LongMap implements BitMap {

    private long[] bits;

    private int size;

    public LongMap(int size) {
        this.bits = new long[size];
        this.size = size;
    }

    @Override
    public boolean get(int index) {
        int row = index / Long.SIZE;
        int column = index % Long.SIZE;
        if (((bits[row] >>> column) & 1) == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void set(int index) {
        int row = index / Long.SIZE;
        int column = index % Long.SIZE;
        bits[row] = bits[row] | (1 << column);
    }

    @Override
    public void unset(int index) {
        int row = index / Long.SIZE;
        int column = index % Long.SIZE;
        bits[row] &= ~(1 << column);
    }

    @Override
    public int size() {
        return size;
    }

}