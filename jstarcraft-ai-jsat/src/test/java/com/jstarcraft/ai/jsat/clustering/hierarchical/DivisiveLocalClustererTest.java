package com.jstarcraft.ai.jsat.clustering.hierarchical;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.SimpleDataSet;
import com.jstarcraft.ai.jsat.classifiers.DataPoint;
import com.jstarcraft.ai.jsat.clustering.evaluation.DaviesBouldinIndex;
import com.jstarcraft.ai.jsat.clustering.kmeans.ElkanKMeans;
import com.jstarcraft.ai.jsat.distributions.Uniform;
import com.jstarcraft.ai.jsat.linear.distancemetrics.DistanceMetric;
import com.jstarcraft.ai.jsat.linear.distancemetrics.EuclideanDistance;
import com.jstarcraft.ai.jsat.utils.GridDataGenerator;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 *
 * @author Edward Raff
 */
public class DivisiveLocalClustererTest {

    static private DivisiveLocalClusterer dlc;
    static private SimpleDataSet easyData;

    public DivisiveLocalClustererTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        DistanceMetric dm = new EuclideanDistance();
        dlc = new DivisiveLocalClusterer(new ElkanKMeans(dm), new DaviesBouldinIndex(dm));
        GridDataGenerator gdg = new GridDataGenerator(new Uniform(-0.15, 0.15), new Random(12), 2, 2);
        easyData = gdg.generateData(100);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of cluster method, of class KMeans.
     */
    @Test
    public void testCluster_DataSet_int() {
        System.out.println("cluster(dataset, int)");
        List<List<DataPoint>> clusters = dlc.cluster(easyData, 10);
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
    public void testCluster_DataSet_int_ExecutorService() {
        System.out.println("cluster(dataset, int, ExecutorService)");
        List<List<DataPoint>> clusters = dlc.cluster(easyData, 10, true);
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
