package com.jstarcraft.ai.jsat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.SimpleDataSet;
import com.jstarcraft.ai.jsat.classifiers.DataPoint;
import com.jstarcraft.ai.jsat.distributions.Normal;
import com.jstarcraft.ai.jsat.linear.distancemetrics.EuclideanDistance;
import com.jstarcraft.ai.jsat.utils.GridDataGenerator;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 *
 * @author Edward Raff
 */
public class FLAMETest {

    public FLAMETest() {
    }

    static private FLAME algo;
    static private SimpleDataSet easyData10;

    @BeforeClass
    public static void setUpClass() throws Exception {
        algo = new FLAME(new EuclideanDistance(), 30, 800);
        GridDataGenerator gdg = new GridDataGenerator(new Normal(0, 0.05), new Random(12), 2, 5);
        easyData10 = gdg.generateData(100);
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
        Clusterer toUse = algo.clone();
        List<List<DataPoint>> clusters = toUse.cluster(easyData10);
        assertEquals(10, clusters.size());
        IntOpenHashSet seenBefore = new IntOpenHashSet();
        for (List<DataPoint> cluster : clusters) {
            int thisClass = cluster.get(0).getCategoricalValue(0);
            assertFalse(seenBefore.contains(thisClass));
            for (DataPoint dp : cluster)
                assertEquals(thisClass, dp.getCategoricalValue(0));
        }
    }

    @Test
    public void testCluster_DataSet_ExecutorService() {
        System.out.println("cluster(dataset, ExecutorService)");
        Clusterer toUse = algo.clone();
        List<List<DataPoint>> clusters = toUse.cluster(easyData10, true);
        assertEquals(10, clusters.size());
        IntOpenHashSet seenBefore = new IntOpenHashSet();
        for (List<DataPoint> cluster : clusters) {
            int thisClass = cluster.get(0).getCategoricalValue(0);
            assertFalse(seenBefore.contains(thisClass));
            for (DataPoint dp : cluster)
                assertEquals(thisClass, dp.getCategoricalValue(0));
        }
    }
}
