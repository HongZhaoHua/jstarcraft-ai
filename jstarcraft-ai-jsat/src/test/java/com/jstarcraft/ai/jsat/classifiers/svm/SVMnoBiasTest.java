package com.jstarcraft.ai.jsat.classifiers.svm;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.FixedProblems;
import com.jstarcraft.ai.jsat.classifiers.ClassificationDataSet;
import com.jstarcraft.ai.jsat.classifiers.svm.SVMnoBias;
import com.jstarcraft.ai.jsat.classifiers.svm.SupportVectorLearner;
import com.jstarcraft.ai.jsat.distributions.kernels.RBFKernel;
import com.jstarcraft.ai.jsat.utils.random.RandomUtil;

/**
 *
 * @author Edward Raff
 */
public class SVMnoBiasTest {
    public SVMnoBiasTest() {
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
        ClassificationDataSet trainSet = FixedProblems.getInnerOuterCircle(150, new Random(2));
        ClassificationDataSet testSet = FixedProblems.getInnerOuterCircle(50, new Random(3));

        for (SupportVectorLearner.CacheMode cacheMode : SupportVectorLearner.CacheMode.values()) {
            SVMnoBias classifier = new SVMnoBias(new RBFKernel(0.5));
            classifier.setCacheMode(cacheMode);
            classifier.setC(10);
            classifier.train(trainSet, true);

            for (int i = 0; i < testSet.size(); i++)
                assertEquals(testSet.getDataPointCategory(i), classifier.classify(testSet.getDataPoint(i)).mostLikely());
        }
    }

    @Test
    public void testTrainC_ClassificationDataSet() {
        System.out.println("trainC");
        ClassificationDataSet trainSet = FixedProblems.getInnerOuterCircle(150, new Random(2));
        ClassificationDataSet testSet = FixedProblems.getInnerOuterCircle(50, new Random(3));

        for (SupportVectorLearner.CacheMode cacheMode : SupportVectorLearner.CacheMode.values()) {
            SVMnoBias classifier = new SVMnoBias(new RBFKernel(0.5));
            classifier.setCacheMode(cacheMode);
            classifier.setC(10);
            classifier.train(trainSet);

            for (int i = 0; i < testSet.size(); i++)
                assertEquals(testSet.getDataPointCategory(i), classifier.classify(testSet.getDataPoint(i)).mostLikely());

            // test warm start off corrupted solution
            double[] a = classifier.alphas;
            Random rand = RandomUtil.getRandom();
            for (int i = 0; i < a.length; i++)
                a[i] = min(max(a[i] + rand.nextDouble() * 2 - 1, 0), 10);

            SVMnoBias classifier2 = new SVMnoBias(new RBFKernel(0.5));
            classifier2.setCacheMode(cacheMode);
            classifier2.setC(10);
            classifier2.train(trainSet, a);

            for (int i = 0; i < testSet.size(); i++)
                assertEquals(testSet.getDataPointCategory(i), classifier2.classify(testSet.getDataPoint(i)).mostLikely());
        }
    }

}
