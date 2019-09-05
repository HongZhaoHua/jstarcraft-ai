package com.jstarcraft.ai.jsat.math.optimization;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.linear.DenseVector;
import com.jstarcraft.ai.jsat.linear.Vec;
import com.jstarcraft.ai.jsat.math.FunctionVec;
import com.jstarcraft.ai.jsat.math.optimization.BFGS;
import com.jstarcraft.ai.jsat.math.optimization.BacktrackingArmijoLineSearch;
import com.jstarcraft.ai.jsat.math.optimization.LineSearch;
import com.jstarcraft.ai.jsat.math.optimization.RosenbrockFunction;
import com.jstarcraft.ai.jsat.math.optimization.WolfeNWLineSearch;
import com.jstarcraft.ai.jsat.utils.random.RandomUtil;

/**
 *
 * @author Edward Raff
 */
public class BFGSTest {

    public BFGSTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of optimize method, of class BFGS.
     */
    @Test
    public void testOptimize() {
        System.out.println("optimize");
        Random rand = RandomUtil.getRandom();
        Vec x0 = new DenseVector(3);// D=3 means one local minima for easy evaluation
        for (int i = 0; i < x0.length(); i++)
            x0.set(i, rand.nextDouble() + 0.5);// make sure we get to the right local optima

        RosenbrockFunction f = new RosenbrockFunction();
        FunctionVec fp = f.getDerivative();
        BFGS instance = new BFGS();

        for (LineSearch lineSearch : new LineSearch[] { new BacktrackingArmijoLineSearch(), new WolfeNWLineSearch() }) {
            instance.setLineSearch(lineSearch);
            Vec w = new DenseVector(x0.length());
            instance.optimize(1e-4, w, x0, f, fp);

            for (int i = 0; i < w.length(); i++)
                assertEquals(1.0, w.get(i), 1e-4);
            assertEquals(0.0, f.f(w), 1e-4);
        }
    }
}
