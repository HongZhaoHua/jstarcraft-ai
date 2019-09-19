package be.tarsos.lsh.families;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import be.tarsos.lsh.Vector;

public class CityBlockDistanceTestCase {

    @Test
    public void testDistance() {
        Vector v = new Vector("left", new float[] { 1F, 2F, 3F });
        Vector other = new Vector("right", new float[] { 3F, 5F, 7F });

        CityBlockDistance distance = new CityBlockDistance();
        float distanceValue = distance.distance(v, other);
        assertEquals(9F, distanceValue, 0F);
        assertEquals(distance.distance(other, v), distanceValue, 0F);
        assertEquals(distance.distance(other, other), 0F, 0F);
    }

}
