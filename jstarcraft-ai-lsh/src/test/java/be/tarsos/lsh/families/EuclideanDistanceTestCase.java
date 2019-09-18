package be.tarsos.lsh.families;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import be.tarsos.lsh.Vector;

public class EuclideanDistanceTestCase {

    @Test
    public void testDistance() {
        Vector v = new Vector("left", new float[] { 1F, 2F, 3F });
        Vector other = new Vector("left", new float[] { 3F, 5F, 7F });

        EuclideanDistance distance = new EuclideanDistance();
        float distanceValue = distance.distance(v, other);
        assertEquals(5.3851647F, distanceValue, 0F);
        assertEquals(distance.distance(other, v), distanceValue, 0F);
        assertEquals(distance.distance(other, other), 0F, 0F);
    }

}
