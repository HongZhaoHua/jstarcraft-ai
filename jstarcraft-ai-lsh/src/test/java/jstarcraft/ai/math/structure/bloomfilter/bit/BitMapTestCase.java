package jstarcraft.ai.math.structure.bloomfilter.bit;

import org.junit.Assert;
import org.junit.Test;

public class BitMapTestCase {

    @Test
    public void testIntegerMap() {
        IntegerMap bits = new IntegerMap(Integer.SIZE);
        Assert.assertEquals(Integer.SIZE, bits.capacity());
        Assert.assertEquals(0, bits.size());
        for (int index = 0; index < Integer.SIZE; index++) {
            Assert.assertFalse(bits.get(index));
            bits.set(index);
            Assert.assertTrue(bits.get(index));
            bits.unset(index);
            Assert.assertFalse(bits.get(index));
        }

        bits.set(0);
        Assert.assertEquals(1, bits.size());
        bits.set(0);
        Assert.assertEquals(1, bits.size());
        bits.unset(Integer.SIZE - 1);
        Assert.assertEquals(1, bits.size());
        bits.unset(0);
        Assert.assertEquals(0, bits.size());
    }

    @Test
    public void testLongMap() {
        LongMap bits = new LongMap(Long.SIZE);
        Assert.assertEquals(Long.SIZE, bits.capacity());
        Assert.assertEquals(0, bits.size());
        for (int index = 0; index < Long.SIZE; index++) {
            Assert.assertFalse(bits.get(index));
            bits.set(index);
            Assert.assertTrue(bits.get(index));
            bits.unset(index);
            Assert.assertFalse(bits.get(index));
        }

        bits.set(0);
        Assert.assertEquals(1, bits.size());
        bits.set(0);
        Assert.assertEquals(1, bits.size());
        bits.unset(Long.SIZE - 1);
        Assert.assertEquals(1, bits.size());
        bits.unset(0);
        Assert.assertEquals(0, bits.size());
    }

}
