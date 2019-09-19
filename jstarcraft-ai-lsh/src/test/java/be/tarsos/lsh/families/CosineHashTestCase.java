package be.tarsos.lsh.families;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import be.tarsos.lsh.Vector;

public class CosineHashTestCase {

    @Test
    public void testHash() {
        Vector v = new Vector(3);
        v.setValue(0, 1);
        v.setValue(1, 2);
        v.setValue(2, 3);

        Random rand = new Random(0);
        CosineHash hash = new CosineHash(rand, 3);
        int hashValue = hash.hash(v);
        assertEquals("Expected about 1", 1, hashValue);
    }

}
