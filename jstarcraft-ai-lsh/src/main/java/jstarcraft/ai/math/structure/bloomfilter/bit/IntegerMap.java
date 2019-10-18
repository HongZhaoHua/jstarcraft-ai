package jstarcraft.ai.math.structure.bloomfilter.bit;

/**
 * 
 * @author Birdy
 *
 */
public class IntegerMap implements BitMap {

    private int[] bits;

    private int size;

    public IntegerMap(int size) {
        this.bits = new int[size];
        this.size = size;
    }

    @Override
    public boolean get(int index) {
        int row = index / Integer.SIZE;
        int column = index % Integer.SIZE;
        if (((bits[row] >>> column) & 1) == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void set(int index) {
        int row = index / Integer.SIZE;
        int column = index % Integer.SIZE;
        bits[row] = bits[row] | (1 << column);
    }

    @Override
    public void unset(int index) {
        int row = index / Integer.SIZE;
        int column = index % Integer.SIZE;
        bits[row] &= ~(1 << column);
    }

    @Override
    public int size() {
        return size;
    }

}