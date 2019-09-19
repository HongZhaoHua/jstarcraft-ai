package be.tarsos.lsh.families;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import be.tarsos.lsh.KeyVector;

public class EuclideanHashTestCase {

    @Test
    public void testHash() {
        KeyVector v = new KeyVector("hash", 3);
        v.setValue(0, 1);
        v.setValue(1, 2);
        v.setValue(2, 3);

        Random rand = new Random(0);
        EuclideanHash hash = new EuclideanHash(rand, 3, 4);
        int hashValue = hash.hash(v);
        assertEquals("Expected about 1", 1, hashValue);
    }

}
