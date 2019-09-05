package com.jstarcraft.ai.jsat.classifiers.linear.kernelized;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.FixedProblems;
import com.jstarcraft.ai.jsat.classifiers.ClassificationDataSet;
import com.jstarcraft.ai.jsat.classifiers.ClassificationModelEvaluation;
import com.jstarcraft.ai.jsat.classifiers.DataPoint;
import com.jstarcraft.ai.jsat.classifiers.linear.kernelized.Projectron;
import com.jstarcraft.ai.jsat.distributions.kernels.RBFKernel;
import com.jstarcraft.ai.jsat.utils.SystemInfo;
import com.jstarcraft.ai.jsat.utils.random.RandomUtil;

/**
 *
 * @author Edward Raff
 */
public class ProjectronTest {
    static private ExecutorService ex;

    public ProjectronTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        ex = Executors.newFixedThreadPool(SystemInfo.LogicalCores);
    }

    @AfterClass
    public static void tearDownClass() {
        ex.shutdown();
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

        for (boolean useMargin : new boolean[] { true, false }) {
            Projectron instance = new Projectron(new RBFKernel(0.5));
            instance.setUseMarginUpdates(useMargin);

            ClassificationDataSet train = FixedProblems.getInnerOuterCircle(1000, RandomUtil.getRandom());
            // add some miss labled data to get the error code to cick in and get exercised
            for (int i = 0; i < 500; i += 20) {
                DataPoint dp = train.getDataPoint(i);
                int y = train.getDataPointCategory(i);
                int badY = (y == 0) ? 1 : 0;
                train.addDataPoint(dp, badY);
            }

            ClassificationDataSet test = FixedProblems.getInnerOuterCircle(100, RandomUtil.getRandom());

            ClassificationModelEvaluation cme = new ClassificationModelEvaluation(instance, train, true);
            cme.evaluateTestSet(test);

            assertEquals(0, cme.getErrorRate(), 0.3);// given some leway due to label noise
        }

    }

    @Test
    public void testTrainC_ClassificationDataSet() {
        System.out.println("trainC");

        for (boolean useMargin : new boolean[] { true, false }) {
            Projectron instance = new Projectron(new RBFKernel(0.5));
            instance.setUseMarginUpdates(useMargin);

            ClassificationDataSet train = FixedProblems.getInnerOuterCircle(1000, RandomUtil.getRandom());
            // add some miss labled data to get the error code to cick in and get exercised
            for (int i = 0; i < 500; i += 20) {
                DataPoint dp = train.getDataPoint(i);
                int y = train.getDataPointCategory(i);
                int badY = (y == 0) ? 1 : 0;
                train.addDataPoint(dp, badY);
            }

            ClassificationDataSet test = FixedProblems.getInnerOuterCircle(100, RandomUtil.getRandom());
            ClassificationModelEvaluation cme = new ClassificationModelEvaluation(instance, train);
            cme.evaluateTestSet(test);

            assertEquals(0, cme.getErrorRate(), 0.3);// given some leway due to label noise
        }
    }

    @Test
    public void testClone() {
        System.out.println("clone");

        Projectron instance = new Projectron(new RBFKernel(0.5));

        ClassificationDataSet t1 = FixedProblems.getInnerOuterCircle(500, RandomUtil.getRandom());
        ClassificationDataSet t2 = FixedProblems.getInnerOuterCircle(500, RandomUtil.getRandom(), 2.0, 10.0);

        instance = instance.clone();

        instance.train(t1);

        Projectron result = instance.clone();

        for (int i = 0; i < t1.size(); i++)
            assertEquals(t1.getDataPointCategory(i), result.classify(t1.getDataPoint(i)).mostLikely());
        result.train(t2);

        for (int i = 0; i < t1.size(); i++)
            assertEquals(t1.getDataPointCategory(i), instance.classify(t1.getDataPoint(i)).mostLikely());

        for (int i = 0; i < t2.size(); i++)
            assertEquals(t2.getDataPointCategory(i), result.classify(t2.getDataPoint(i)).mostLikely());

    }
}
