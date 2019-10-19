package jstarcraft.ai.math.structure.bloomfilter.bit;

public class LongMap implements BitMap {

    private long[] bits;

    private int size;

    public LongMap(int size) {
        assert size > 0;
        int elements = size % Long.SIZE == 0 ? size / Long.SIZE : size / Long.SIZE + 1;
        this.bits = new long[elements];
        this.size = size;
    }

    @Override
    public boolean get(int index) {
        int row = index / Long.SIZE;
        int column = index % Long.SIZE;
        if (((bits[row] >>> column) & 1L) == 1L) {
            return true;
        }
        return false;
    }

    @Override
    public void set(int index) {
        int row = index / Long.SIZE;
        int column = index % Long.SIZE;
        bits[row] |= (1L << column);
    }

    @Override
    public void unset(int index) {
        int row = index / Long.SIZE;
        int column = index % Long.SIZE;
        bits[row] &= ~(1L << column);
    }

    @Override
    public int size() {
        return size;
    }

}