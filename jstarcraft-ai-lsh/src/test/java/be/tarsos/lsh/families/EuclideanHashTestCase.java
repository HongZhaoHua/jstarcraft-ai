package be.tarsos.lsh.families;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import be.tarsos.lsh.Vector;

public class EuclideanHashTestCase {

    @Test
    public void testHash() {
        Vector v = new Vector(3);
        v.set(0, 1);
        v.set(1, 2);
        v.set(2, 3);

        Random rand = new Random(0);
        EuclideanHash hash = new EuclideanHash(rand, 3, 4);
        int hashValue = hash.hash(v);
        assertEquals("Expected about 1", 1, hashValue);
    }

}
