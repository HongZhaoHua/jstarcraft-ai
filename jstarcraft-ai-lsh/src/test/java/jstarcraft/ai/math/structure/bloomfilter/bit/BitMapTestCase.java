package jstarcraft.ai.math.structure.bloomfilter.bit;

import org.junit.Assert;
import org.junit.Test;

public class BitMapTestCase {

    @Test
    public void testIntegerMap() {
        IntegerMap bits = new IntegerMap(Integer.SIZE);

        for (int index = 0; index < Integer.SIZE; index++) {
            Assert.assertFalse(bits.get(index));
            bits.set(index);
            Assert.assertTrue(bits.get(index));
            bits.unset(index);
            Assert.assertFalse(bits.get(index));
        }
    }

    @Test
    public void testLongMap() {
        LongMap bits = new LongMap(Long.SIZE);

        for (int index = 0; index < Long.SIZE; index++) {
            Assert.assertFalse(bits.get(index));
            bits.set(index);
            Assert.assertTrue(bits.get(index));
            bits.unset(index);
            Assert.assertFalse(bits.get(index));
        }
    }

}
