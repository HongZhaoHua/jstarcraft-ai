/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jstarcraft.ai.jsat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.SimpleDataSet;
import com.jstarcraft.ai.jsat.classifiers.DataPoint;
import com.jstarcraft.ai.jsat.clustering.MeanShift;
import com.jstarcraft.ai.jsat.distributions.Normal;
import com.jstarcraft.ai.jsat.utils.GridDataGenerator;
import com.jstarcraft.ai.jsat.utils.IntSet;

/**
 *
 * @author Edward Raff
 */
public class MeanShiftTest {

    public MeanShiftTest() {
    }

    static private MeanShift meanShift;
    static private SimpleDataSet easyData10;

    @BeforeClass
    public static void setUpClass() throws Exception {
        meanShift = new MeanShift();
        GridDataGenerator gdg = new GridDataGenerator(new Normal(0, 0.10), new Random(12), 2, 5);
        easyData10 = gdg.generateData(40);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

    }

    @Test
    public void testCluster_DataSet() {
        System.out.println("cluster(dataset)");
        List<List<DataPoint>> clusters = meanShift.cluster(easyData10);
        assertEquals(10, clusters.size());
        Set<Integer> seenBefore = new IntSet();
        for (List<DataPoint> cluster : clusters) {
            int thisClass = cluster.get(0).getCategoricalValue(0);
            assertFalse(seenBefore.contains(thisClass));
            seenBefore.add(thisClass);
            for (DataPoint dp : cluster)
                assertEquals(thisClass, dp.getCategoricalValue(0));
        }
    }

    @Test
    public void testCluster_DataSet_ExecutorService() {
        System.out.println("cluster(dataset, ExecutorService)");
        List<List<DataPoint>> clusters = meanShift.cluster(easyData10, true);
        assertEquals(10, clusters.size());
        Set<Integer> seenBefore = new IntSet();
        for (List<DataPoint> cluster : clusters) {
            int thisClass = cluster.get(0).getCategoricalValue(0);
            assertFalse(seenBefore.contains(thisClass));
            seenBefore.add(thisClass);
            for (DataPoint dp : cluster)
                assertEquals(thisClass, dp.getCategoricalValue(0));
        }
    }
}
