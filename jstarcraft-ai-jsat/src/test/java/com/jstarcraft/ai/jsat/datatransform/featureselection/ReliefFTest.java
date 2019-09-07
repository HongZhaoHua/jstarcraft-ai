
package com.jstarcraft.ai.jsat.datatransform.featureselection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.classifiers.ClassificationDataSet;
import com.jstarcraft.ai.jsat.linear.distancemetrics.EuclideanDistance;
import com.jstarcraft.ai.jsat.utils.random.XORWOW;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 *
 * @author Edward Raff
 */
public class ReliefFTest {

    public ReliefFTest() {
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
     * Test of transform method, of class ReliefF.
     */
    @Test
    public void testTransformC() {
        System.out.println("transformC");
        Random rand = new XORWOW(13);
        int t0 = 1, t1 = 5, t2 = 8;
        IntOpenHashSet shouldHave = new IntOpenHashSet();
        shouldHave.addAll(Arrays.asList(t0, t1, t2));

        ClassificationDataSet cds = SFSTest.generate3DimIn10(rand, t0, t1, t2);

        ReliefF relieff = new ReliefF(3, 50, 7, new EuclideanDistance()).clone();
        relieff.fit(cds);
        IntOpenHashSet found = new IntOpenHashSet(relieff.getKeptNumeric());

        assertEquals(shouldHave.size(), found.size());
        assertTrue(shouldHave.containsAll(found));
        cds.applyTransform(relieff);
        assertEquals(shouldHave.size(), cds.getNumFeatures());
    }

}
