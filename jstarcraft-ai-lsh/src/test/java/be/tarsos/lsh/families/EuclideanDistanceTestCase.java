package be.tarsos.lsh.families;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import be.tarsos.lsh.KeyVector;

public class EuclideanDistanceTestCase {

    @Test
    public void testDistance() {
        KeyVector v = new KeyVector("left", new float[] { 1F, 2F, 3F });
        KeyVector other = new KeyVector("right", new float[] { 3F, 5F, 7F });

        EuclideanDistance distance = new EuclideanDistance();
        float distanceValue = distance.distance(v, other);
        assertEquals(5.3851647F, distanceValue, 0F);
        assertEquals(distance.distance(other, v), distanceValue, 0F);
        assertEquals(distance.distance(other, other), 0F, 0F);
    }

}
