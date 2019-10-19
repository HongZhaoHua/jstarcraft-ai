package jstarcraft.ai.math.algorithm.lsh;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import com.jstarcraft.ai.math.structure.vector.ArrayVector;

public class EuclideanHashTestCase {

    @Test
    public void testHash() {
        ArrayVector vector = new ArrayVector(3, new float[] { 1, 2, 3 });

        Random random = new Random(0);
        EuclideanHashFunction hash = new EuclideanHashFunction(random, 3, 4);
        int hashValue = hash.hash(vector);
        assertEquals("Expected about 1", 1, hashValue);
    }

}
