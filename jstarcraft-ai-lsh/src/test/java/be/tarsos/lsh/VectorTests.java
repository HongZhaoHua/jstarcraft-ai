package be.tarsos.lsh;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorTests {

    @Test
    public void testDotProduct() {
        Vector one = new Vector("left", 3);
        Vector two = new Vector("right", 3);
        one.setValue(0, 1);
        two.setValue(0, 7);
        one.setValue(1, 1);
        two.setValue(1, -7);
        one.setValue(2, 0);
        two.setValue(2, 1);
        assertEquals(0, one.dot(two), 0.000000000001);
        assertEquals(two.dot(one), one.dot(two), 0.000000000001);

        one.setValue(2, 3);
        assertEquals(3, one.dot(two), 0.000000000001);
    }

}
