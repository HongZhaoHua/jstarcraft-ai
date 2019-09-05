package com.jstarcraft.ai.jsat.classifiers.svm;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.FixedProblems;
import com.jstarcraft.ai.jsat.classifiers.ClassificationDataSet;
import com.jstarcraft.ai.jsat.classifiers.svm.DCSVM;
import com.jstarcraft.ai.jsat.classifiers.svm.SupportVectorLearner;
import com.jstarcraft.ai.jsat.distributions.kernels.RBFKernel;

/**
 *
 * @author Edward Raff
 */
public class DCSVMTest {
    public DCSVMTest() {
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
        ClassificationDataSet trainSet = FixedProblems.getInnerOuterCircle(600, new Random(2));
        ClassificationDataSet testSet = FixedProblems.getInnerOuterCircle(50, new Random(3));

        for (SupportVectorLearner.CacheMode cacheMode : SupportVectorLearner.CacheMode.values()) {
            DCSVM classifier = new DCSVM(new RBFKernel(0.5));
            classifier.setCacheMode(cacheMode);
            classifier.setC(10);
            classifier.setClusterSampleSize(200);// make smaller to test sub-sampling
            classifier.train(trainSet, true);

            for (int i = 0; i < testSet.size(); i++)
                assertEquals(testSet.getDataPointCategory(i), classifier.classify(testSet.getDataPoint(i)).mostLikely());
        }

        trainSet = FixedProblems.getInnerOuterCircle(150, new Random(2));
        testSet = FixedProblems.getInnerOuterCircle(50, new Random(3));

        for (SupportVectorLearner.CacheMode cacheMode : SupportVectorLearner.CacheMode.values()) {
            DCSVM classifier = new DCSVM(new RBFKernel(0.5));
            classifier.setCacheMode(cacheMode);
            classifier.setC(10);
            classifier.setEndLevel(0);
            classifier.train(trainSet, true);

            for (int i = 0; i < testSet.size(); i++)
                assertEquals(testSet.getDataPointCategory(i), classifier.classify(testSet.getDataPoint(i)).mostLikely());
        }
    }

    @Test
    public void testTrainC_ClassificationDataSet() {
        System.out.println("trainC");
        ClassificationDataSet trainSet = FixedProblems.getInnerOuterCircle(600, new Random(2));
        ClassificationDataSet testSet = FixedProblems.getInnerOuterCircle(50, new Random(3));

        for (SupportVectorLearner.CacheMode cacheMode : SupportVectorLearner.CacheMode.values()) {
            DCSVM classifier = new DCSVM(new RBFKernel(0.5));
            classifier.setCacheMode(cacheMode);
            classifier.setC(10);
            classifier.setClusterSampleSize(200);// make smaller to test sub-sampling
            classifier.train(trainSet);

            for (int i = 0; i < testSet.size(); i++)
                assertEquals(testSet.getDataPointCategory(i), classifier.classify(testSet.getDataPoint(i)).mostLikely());
        }

        trainSet = FixedProblems.getInnerOuterCircle(150, new Random(2));
        testSet = FixedProblems.getInnerOuterCircle(50, new Random(3));

        for (SupportVectorLearner.CacheMode cacheMode : SupportVectorLearner.CacheMode.values()) {
            DCSVM classifier = new DCSVM(new RBFKernel(0.5));
            classifier.setCacheMode(cacheMode);
            classifier.setC(10);
            classifier.setEndLevel(0);
            classifier.train(trainSet);

            for (int i = 0; i < testSet.size(); i++)
                assertEquals(testSet.getDataPointCategory(i), classifier.classify(testSet.getDataPoint(i)).mostLikely());
        }
    }

}
