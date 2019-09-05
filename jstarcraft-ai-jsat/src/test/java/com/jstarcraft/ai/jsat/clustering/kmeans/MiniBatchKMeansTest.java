package com.jstarcraft.ai.jsat.clustering.kmeans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.SimpleDataSet;
import com.jstarcraft.ai.jsat.classifiers.DataPoint;
import com.jstarcraft.ai.jsat.clustering.SeedSelectionMethods;
import com.jstarcraft.ai.jsat.clustering.kmeans.MiniBatchKMeans;
import com.jstarcraft.ai.jsat.distributions.Uniform;
import com.jstarcraft.ai.jsat.linear.distancemetrics.EuclideanDistance;
import com.jstarcraft.ai.jsat.utils.GridDataGenerator;
import com.jstarcraft.ai.jsat.utils.IntSet;

/**
 *
 * @author Edward Raff
 */
public class MiniBatchKMeansTest {
    // NOTE: FARTHER FIST seed + 2 x 2 grid of 4 classes results in a deterministic
    // result given a high density

    static private SimpleDataSet easyData10;

    public MiniBatchKMeansTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        GridDataGenerator gdg = new GridDataGenerator(new Uniform(-0.15, 0.15), new Random(12), 2, 2);
        easyData10 = gdg.generateData(110);
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
     * Test of cluster method, of class MiniBatchKMeans.
     */
    @Test
    public void testCluster_DataSet_intArr() {
        System.out.println("cluster");
        MiniBatchKMeans kMeans = new MiniBatchKMeans(new EuclideanDistance(), 50, 50, SeedSelectionMethods.SeedSelection.FARTHEST_FIRST);
        List<List<DataPoint>> clusters = kMeans.cluster(easyData10, 10);
        assertEquals(10, clusters.size());
        Set<Integer> seenBefore = new IntSet();
        for (List<DataPoint> cluster : clusters) {
            int thisClass = cluster.get(0).getCategoricalValue(0);
            assertFalse(seenBefore.contains(thisClass));
            for (DataPoint dp : cluster)
                assertEquals(thisClass, dp.getCategoricalValue(0));
        }
    }

    /**
     * Test of cluster method, of class MiniBatchKMeans.
     */
    @Test
    public void testCluster_3args_1() {
        System.out.println("cluster");
        MiniBatchKMeans kMeans = new MiniBatchKMeans(new EuclideanDistance(), 50, 50, SeedSelectionMethods.SeedSelection.FARTHEST_FIRST);
        List<List<DataPoint>> clusters = kMeans.cluster(easyData10, 10, true);
        assertEquals(10, clusters.size());
        Set<Integer> seenBefore = new IntSet();
        for (List<DataPoint> cluster : clusters) {
            int thisClass = cluster.get(0).getCategoricalValue(0);
            assertFalse(seenBefore.contains(thisClass));
            for (DataPoint dp : cluster)
                assertEquals(thisClass, dp.getCategoricalValue(0));
        }
    }

}
