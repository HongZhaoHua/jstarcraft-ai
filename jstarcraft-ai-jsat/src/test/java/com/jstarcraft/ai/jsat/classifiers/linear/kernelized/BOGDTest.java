package com.jstarcraft.ai.jsat.classifiers.linear.kernelized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.FixedProblems;
import com.jstarcraft.ai.jsat.classifiers.ClassificationDataSet;
import com.jstarcraft.ai.jsat.classifiers.ClassificationModelEvaluation;
import com.jstarcraft.ai.jsat.classifiers.linear.kernelized.BOGD;
import com.jstarcraft.ai.jsat.distributions.kernels.RBFKernel;
import com.jstarcraft.ai.jsat.lossfunctions.HingeLoss;
import com.jstarcraft.ai.jsat.utils.random.RandomUtil;

/**
 *
 * @author Edward Raff
 */
public class BOGDTest {

    public BOGDTest() {
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
    public void testTrainC_ClassificationDataSet_ExecutorService() {
        System.out.println("trainC");

        for (boolean sampling : new boolean[] { true, false }) {
            BOGD instance = new BOGD(new RBFKernel(0.5), 50, 0.5, 1e-3, 10, new HingeLoss());
            instance.setUniformSampling(sampling);

            ClassificationDataSet train = FixedProblems.getInnerOuterCircle(200, RandomUtil.getRandom(), 1, 4);
            ClassificationDataSet test = FixedProblems.getCircles(100, 0.0, RandomUtil.getRandom(), 1, 4);

            ClassificationModelEvaluation cme = new ClassificationModelEvaluation(instance, train, true);
            cme.evaluateTestSet(test);

            assertEquals(0, cme.getErrorRate(), 0.0);
        }
    }

    @Test
    public void testTrainC_ClassificationDataSet() {
        System.out.println("trainC");

        for (boolean sampling : new boolean[] { true, false }) {
            BOGD instance = new BOGD(new RBFKernel(0.5), 50, 0.5, 1e-3, 10, new HingeLoss());
            instance.setUniformSampling(sampling);

            ClassificationDataSet train = FixedProblems.getInnerOuterCircle(200, RandomUtil.getRandom(), 1, 4);
            ClassificationDataSet test = FixedProblems.getCircles(100, 0.0, RandomUtil.getRandom(), 1, 4);

            ClassificationModelEvaluation cme = new ClassificationModelEvaluation(instance, train);
            cme.evaluateTestSet(test);

            assertEquals(0, cme.getErrorRate(), 0.0);
        }
    }

    @Test
    public void testClone() {
        System.out.println("clone");

        BOGD instance = new BOGD(new RBFKernel(0.5), 50, 0.5, 1e-3, 10, new HingeLoss());

        ClassificationDataSet t1 = FixedProblems.getCircles(500, 0.0, RandomUtil.getRandom(), 1, 4);
        ClassificationDataSet t2 = FixedProblems.getCircles(500, 0.0, RandomUtil.getRandom(), 0.5, 3.0);

        instance.setUniformSampling(true);
        instance = instance.clone();

        instance.train(t1);

        instance.setUniformSampling(false);
        BOGD result = instance.clone();
        assertFalse(result.isUniformSampling());

        for (int i = 0; i < t1.size(); i++)
            assertEquals(t1.getDataPointCategory(i), result.classify(t1.getDataPoint(i)).mostLikely());
        result.train(t2);

        for (int i = 0; i < t1.size(); i++)
            assertEquals(t1.getDataPointCategory(i), instance.classify(t1.getDataPoint(i)).mostLikely());

        for (int i = 0; i < t2.size(); i++)
            assertEquals(t2.getDataPointCategory(i), result.classify(t2.getDataPoint(i)).mostLikely());

    }
}
