
package jsat.math.optimization;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import jsat.linear.DenseVector;
import jsat.linear.Vec;
import jsat.math.Function;

/**
 *
 * @author Edward Raff
 */
public class NelderMeadTest {

    public NelderMeadTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of optimize method, of class NelderMead.
     */
    @Test
    public void testOptimize_8args() {
        System.out.println("optimize");

        NelderMead instance = new NelderMead();
        Function banana = new RosenbrockFunction();
        DenseVector search = DenseVector.toDenseVec(1.05, 0.95, 1.05);
        Vec x = instance.optimize(1e-6, 1000, banana, Arrays.asList(search), true);
        assertEquals(0.0, banana.f(x), 1e-3);// Its a hard function to get, we often get close
    }
}
