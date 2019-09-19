package be.tarsos.lsh.families;

import static org.junit.Assert.*;

import org.junit.Test;

import be.tarsos.lsh.Vector;
import be.tarsos.lsh.families.CosineDistance;

public class CosineDistanceTestCase {

    @Test
    public void testDistance() {
        Vector v = new Vector("left", new float[] { 1F, 2F, 3F });
        Vector other = new Vector("right", new float[] { 3F, 5F, 7F });

        CosineDistance distance = new CosineDistance();
        float distanceValue = distance.distance(v, other);
        assertEquals(0.0025850534F, distanceValue, 0F);
        assertEquals(distance.distance(other, v), distanceValue, 0F);
        assertEquals(distance.distance(other, other), 0F, 0F);
    }

}
