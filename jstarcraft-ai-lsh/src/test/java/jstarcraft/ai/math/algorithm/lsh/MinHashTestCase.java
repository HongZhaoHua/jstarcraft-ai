package jstarcraft.ai.math.algorithm.lsh;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import be.tarsos.lsh.KeyVector;

public class MinHashTestCase {

    @Test
    public void testHash() {
        KeyVector v = new KeyVector("hash", 3);
        v.setValue(0, 1);
        v.setValue(1, 2);
        v.setValue(2, 3);

        Random rand = new Random(0);
        MinHash hash = new MinHash(rand);
        int hashValue = hash.hash(v);
        assertEquals("Expected about -939719986", -939719986, hashValue);
    }

}
