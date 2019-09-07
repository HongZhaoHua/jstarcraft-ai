/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jstarcraft.ai.jsat.clustering.hierarchical;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.SimpleDataSet;
import com.jstarcraft.ai.jsat.classifiers.DataPoint;
import com.jstarcraft.ai.jsat.clustering.evaluation.DaviesBouldinIndex;
import com.jstarcraft.ai.jsat.clustering.kmeans.NaiveKMeans;
import com.jstarcraft.ai.jsat.distributions.Uniform;
import com.jstarcraft.ai.jsat.linear.distancemetrics.DistanceMetric;
import com.jstarcraft.ai.jsat.linear.distancemetrics.EuclideanDistance;
import com.jstarcraft.ai.jsat.utils.GridDataGenerator;
import com.jstarcraft.ai.jsat.utils.random.RandomUtil;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 *
 * @author Edward Raff
 */
public class DivisiveGlobalClustererTest {
    static private DivisiveGlobalClusterer dgc;
    static private SimpleDataSet easyData;

    public DivisiveGlobalClustererTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        GridDataGenerator gdg = new GridDataGenerator(new Uniform(-0.05, 0.05), RandomUtil.getRandom(), 2, 2);
        easyData = gdg.generateData(60);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        DistanceMetric dm = new EuclideanDistance();
        dgc = new DivisiveGlobalClusterer(new NaiveKMeans(), new DaviesBouldinIndex(dm));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCluster_DataSet_int() {
        System.out.println("cluster(dataset, int)");
        List<List<DataPoint>> clusters = dgc.cluster(easyData, 4);
        assertEquals(4, clusters.size());
        IntOpenHashSet seenBefore = new IntOpenHashSet();
        for (List<DataPoint> cluster : clusters) {
            int thisClass = cluster.get(0).getCategoricalValue(0);
            assertFalse(seenBefore.contains(thisClass));
            for (DataPoint dp : cluster)
                assertEquals(thisClass, dp.getCategoricalValue(0));
        }
    }

    @Test
    public void testCluster_DataSet() {
        System.out.println("cluster(dataset)");
        List<List<DataPoint>> clusters = dgc.cluster(easyData);
        assertEquals(4, clusters.size());
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
        List<List<DataPoint>> clusters = dgc.cluster(easyData, true);
        assertEquals(4, clusters.size());
        IntOpenHashSet seenBefore = new IntOpenHashSet();
        for (List<DataPoint> cluster : clusters) {
            int thisClass = cluster.get(0).getCategoricalValue(0);
            assertFalse(seenBefore.contains(thisClass));
            for (DataPoint dp : cluster)
                assertEquals(thisClass, dp.getCategoricalValue(0));
        }
    }

    @Test
    public void testCluster_DataSet_int_int() {
        System.out.println("cluster(dataset, int, int)");
        List<List<DataPoint>> clusters = dgc.cluster(easyData, 2, 20);
        assertEquals(4, clusters.size());
        IntOpenHashSet seenBefore = new IntOpenHashSet();
        for (List<DataPoint> cluster : clusters) {
            int thisClass = cluster.get(0).getCategoricalValue(0);
            assertFalse(seenBefore.contains(thisClass));
            for (DataPoint dp : cluster)
                assertEquals(thisClass, dp.getCategoricalValue(0));
        }
    }

    @Test
    public void testCluster_DataSet_int_int_ExecutorService() {
        System.out.println("cluster(dataset, int, int, ExecutorService)");
        List<List<DataPoint>> clusters = dgc.cluster(easyData, 2, 20, true);
        assertEquals(4, clusters.size());
        IntOpenHashSet seenBefore = new IntOpenHashSet();
        for (List<DataPoint> cluster : clusters) {
            int thisClass = cluster.get(0).getCategoricalValue(0);
            assertFalse(seenBefore.contains(thisClass));
            for (DataPoint dp : cluster)
                assertEquals(thisClass, dp.getCategoricalValue(0));
        }
    }

    @Test
    public void testCluster_DataSet_int_ExecutorService() {
        System.out.println("cluster(dataset, int, ExecutorService)");
        List<List<DataPoint>> clusters = dgc.cluster(easyData, 4, true);
        assertEquals(4, clusters.size());
        IntOpenHashSet seenBefore = new IntOpenHashSet();
        for (List<DataPoint> cluster : clusters) {
            int thisClass = cluster.get(0).getCategoricalValue(0);
            assertFalse(seenBefore.contains(thisClass));
            for (DataPoint dp : cluster)
                assertEquals(thisClass, dp.getCategoricalValue(0));
        }
    }
}
