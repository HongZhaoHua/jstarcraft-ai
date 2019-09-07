package com.jstarcraft.ai.jsat.linear.vectorcollection.lsh;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.linear.DenseVector;
import com.jstarcraft.ai.jsat.linear.Vec;
import com.jstarcraft.ai.jsat.linear.VecPaired;
import com.jstarcraft.ai.jsat.linear.distancemetrics.CosineDistanceNormalized;
import com.jstarcraft.ai.jsat.linear.vectorcollection.VectorArray;
import com.jstarcraft.ai.jsat.math.OnLineStatistics;
import com.jstarcraft.ai.jsat.utils.random.RandomUtil;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 *
 * @author Edward Raff
 */
public class RandomProjectionLSHTest {

    public RandomProjectionLSHTest() {
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
     * Test of search method, of class RandomProjectionLSH.
     */
    @Test
    public void testSearch_Vec_double() {
        System.out.println("search");

        List<VecPaired<Vec, Integer>> normalVecs = new ArrayList<VecPaired<Vec, Integer>>();
        Random rand = RandomUtil.getRandom();

        for (int i = 0; i < 100; i++) {
            DenseVector dv = new DenseVector(20);
            for (int j = 0; j < dv.length(); j++)
                dv.set(j, rand.nextGaussian());
            dv.normalize();
            normalVecs.add(new VecPaired<Vec, Integer>(dv, i));
        }

        CosineDistanceNormalized dm = new CosineDistanceNormalized();

        VectorArray<VecPaired<Vec, Integer>> naiveVC = new VectorArray<VecPaired<Vec, Integer>>(dm, normalVecs);
        RandomProjectionLSH<VecPaired<Vec, Integer>> rpVC = new RandomProjectionLSH<VecPaired<Vec, Integer>>(normalVecs, 16, true);

        OnLineStatistics knnStats = new OnLineStatistics();
        for (Vec v : normalVecs)
            knnStats.add(naiveVC.search(v, 11).get(10).getPair());// first nn is itselft

        double searchDist = knnStats.getMean() + knnStats.getStandardDeviation() * 2;
        IntOpenHashSet inTruth = new IntOpenHashSet();

        for (Vec v : normalVecs)// now use the stats to compare results
        {
            List<? extends VecPaired<VecPaired<Vec, Integer>, Double>> trueResults = naiveVC.search(v, searchDist);
            List<? extends VecPaired<VecPaired<Vec, Integer>, Double>> aprxResults = rpVC.search(v, searchDist);

            inTruth.clear();
            for (VecPaired<VecPaired<Vec, Integer>, Double> vp : trueResults)
                inTruth.add(vp.getVector().getPair());
            int contained = 0;
            for (VecPaired<VecPaired<Vec, Integer>, Double> vp : aprxResults)
                if (inTruth.contains(vp.getVector().getPair()))
                    contained++;

            // Recall must be at least 0.5, should be an easy target
            assertTrue(contained >= inTruth.size() / 2);
        }
    }

    /**
     * Test of search method, of class RandomProjectionLSH.
     */
    @Test
    public void testSearch_Vec_int() {
        System.out.println("search");
        List<VecPaired<Vec, Integer>> normalVecs = new ArrayList<VecPaired<Vec, Integer>>();
        Random rand = RandomUtil.getRandom();

        for (int i = 0; i < 100; i++) {
            DenseVector dv = new DenseVector(20);
            for (int j = 0; j < dv.length(); j++)
                dv.set(j, rand.nextGaussian());
            dv.normalize();
            normalVecs.add(new VecPaired<Vec, Integer>(dv, i));
        }

        CosineDistanceNormalized dm = new CosineDistanceNormalized();

        VectorArray<VecPaired<Vec, Integer>> naiveVC = new VectorArray<VecPaired<Vec, Integer>>(dm, normalVecs);
        RandomProjectionLSH<VecPaired<Vec, Integer>> rpVC = new RandomProjectionLSH<VecPaired<Vec, Integer>>(normalVecs, 16, true);

        IntOpenHashSet inTruth = new IntOpenHashSet();

        for (Vec v : normalVecs)// now use the stats to compare results
        {
            List<? extends VecPaired<VecPaired<Vec, Integer>, Double>> trueResults = naiveVC.search(v, 15);
            List<? extends VecPaired<VecPaired<Vec, Integer>, Double>> aprxResults = rpVC.search(v, 15);

            inTruth.clear();
            for (VecPaired<VecPaired<Vec, Integer>, Double> vp : trueResults)
                inTruth.add(vp.getVector().getPair());
            int contained = 0;
            for (VecPaired<VecPaired<Vec, Integer>, Double> vp : aprxResults)
                if (inTruth.contains(vp.getVector().getPair()))
                    contained++;

            // Recall must be at least 0.5, should be an easy target
            assertTrue(contained >= inTruth.size() / 2);
        }
    }

}
