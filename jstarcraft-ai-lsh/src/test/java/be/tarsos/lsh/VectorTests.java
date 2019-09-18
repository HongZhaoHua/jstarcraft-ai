package be.tarsos.lsh;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorTests {

    @Test
    public void testDotProduct() {
        Vector one = new Vector(3);
        Vector two = new Vector(3);
        one.set(0, 1);
        two.set(0, 7);
        one.set(1, 1);
        two.set(1, -7);
        one.set(2, 0);
        two.set(2, 1);
        assertEquals(0, one.dot(two), 0.000000000001);
        assertEquals(two.dot(one), one.dot(two), 0.000000000001);

        one.set(2, 3);
        assertEquals(3, one.dot(two), 0.000000000001);
    }

}
