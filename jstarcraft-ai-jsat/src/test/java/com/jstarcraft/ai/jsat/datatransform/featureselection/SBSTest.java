
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
import com.jstarcraft.ai.jsat.classifiers.Classifier;
import com.jstarcraft.ai.jsat.classifiers.knn.NearestNeighbour;
import com.jstarcraft.ai.jsat.regression.MultipleLinearRegression;
import com.jstarcraft.ai.jsat.regression.RegressionDataSet;
import com.jstarcraft.ai.jsat.utils.random.XORWOW;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 *
 * @author Edward Raff
 */
public class SBSTest {

    public SBSTest() {
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

    @Test
    public void testTransformC() {
        System.out.println("transformC");
        Random rand = new XORWOW(13);
        int t0 = 1, t1 = 5, t2 = 8;

        ClassificationDataSet cds = SFSTest.generate3DimIn10(rand, t0, t1, t2);

        SBS sbs = new SBS(1, 7, (Classifier) new NearestNeighbour(7), 1e-3).clone();
        sbs.setFolds(5);
        sbs.fit(cds);
        IntSet found = sbs.getSelectedNumerical();

        IntOpenHashSet shouldHave = new IntOpenHashSet();
        shouldHave.addAll(Arrays.asList(t0, t1, t2));
        assertEquals(shouldHave.size(), found.size());
        assertTrue(shouldHave.containsAll(found));
        cds.applyTransform(sbs);
        assertEquals(3, cds.getNumFeatures());
    }

    @Test
    public void testTransformR() {
        System.out.println("transformR");
        Random rand = new XORWOW(13);
        int t0 = 1, t1 = 5, t2 = 8;

        RegressionDataSet cds = SFSTest.generate3DimIn10R(rand, t0, t1, t2);

        SBS sbs = new SBS(1, 7, new MultipleLinearRegression(), 1.0).clone();
        sbs.setFolds(5);
        sbs.fit(cds);
        IntSet found = sbs.getSelectedNumerical();

        IntOpenHashSet shouldHave = new IntOpenHashSet();
        shouldHave.addAll(Arrays.asList(t0, t1, t2));
        assertEquals(shouldHave.size(), found.size());
        assertTrue(shouldHave.containsAll(found));
        cds.applyTransform(sbs);
        assertEquals(3, cds.getNumFeatures());
    }
}
