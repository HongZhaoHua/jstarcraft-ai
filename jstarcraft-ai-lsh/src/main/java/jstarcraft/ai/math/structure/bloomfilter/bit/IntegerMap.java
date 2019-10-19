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
        assert size > 0;
        int elements = size % Integer.SIZE == 0 ? size / Integer.SIZE : size / Integer.SIZE + 1;
        this.bits = new int[elements];
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
        bits[row] |= (1 << column);
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