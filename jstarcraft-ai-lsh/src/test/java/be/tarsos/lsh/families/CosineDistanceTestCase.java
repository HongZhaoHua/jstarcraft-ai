package be.tarsos.lsh.families;

import static org.junit.Assert.*;

import org.junit.Test;

import be.tarsos.lsh.KeyVector;
import be.tarsos.lsh.families.CosineDistance;

public class CosineDistanceTestCase {

    @Test
    public void testDistance() {
        KeyVector v = new KeyVector("left", new float[] { 1F, 2F, 3F });
        KeyVector other = new KeyVector("right", new float[] { 3F, 5F, 7F });

        CosineDistance distance = new CosineDistance();
        float distanceValue = distance.distance(v, other);
        assertEquals(0.0025850534F, distanceValue, 0F);
        assertEquals(distance.distance(other, v), distanceValue, 0F);
        assertEquals(distance.distance(other, other), 0F, 0F);
    }

}
