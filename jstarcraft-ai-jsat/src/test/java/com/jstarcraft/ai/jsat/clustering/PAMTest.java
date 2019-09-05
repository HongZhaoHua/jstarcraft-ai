/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jstarcraft.ai.jsat.clustering;

import static com.jstarcraft.ai.jsat.TestTools.checkClusteringByCat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.SimpleDataSet;
import com.jstarcraft.ai.jsat.classifiers.DataPoint;
import com.jstarcraft.ai.jsat.clustering.PAM;
import com.jstarcraft.ai.jsat.clustering.SeedSelectionMethods.SeedSelection;
import com.jstarcraft.ai.jsat.distributions.Uniform;
import com.jstarcraft.ai.jsat.linear.distancemetrics.EuclideanDistance;
import com.jstarcraft.ai.jsat.utils.GridDataGenerator;
import com.jstarcraft.ai.jsat.utils.random.RandomUtil;

/**
 *
 * @author Edward Raff
 */
public class PAMTest {
    // Like KMeans the cluster number detection isnt stable enough yet that we can
    // test that it getst he right result.
    static private PAM pam;
    static private SimpleDataSet easyData10;

    public PAMTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        pam = new PAM(new EuclideanDistance(), RandomUtil.getRandom(), SeedSelection.FARTHEST_FIRST);
        pam.setMaxIterations(1000);
        GridDataGenerator gdg = new GridDataGenerator(new Uniform(-0.05, 0.05), RandomUtil.getRandom(), 2, 5);
        easyData10 = gdg.generateData(100);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of cluster method, of class PAM.
     */
    @Test
    public void testCluster_3args_1() {
        System.out.println("cluster(dataSet, int, ExecutorService)");
        boolean good = false;
        int count = 0;
        do {
            List<List<DataPoint>> clusters = pam.cluster(easyData10, 10, true);
            assertEquals(10, clusters.size());
            good = checkClusteringByCat(clusters);
        } while (!good && count++ < 3);
        assertTrue(good);
    }

    /**
     * Test of cluster method, of class PAM.
     */
    @Test
    public void testCluster_DataSet_int() {
        System.out.println("cluster(dataset, int)");
        boolean good = false;
        int count = 0;
        do {
            List<List<DataPoint>> clusters = pam.cluster(easyData10, 10);
            assertEquals(10, clusters.size());
            good = checkClusteringByCat(clusters);
        } while (!good && count++ < 3);
        assertTrue(good);
    }

}
