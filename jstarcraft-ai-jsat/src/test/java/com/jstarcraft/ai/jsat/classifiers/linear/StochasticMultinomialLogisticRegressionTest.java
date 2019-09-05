/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jstarcraft.ai.jsat.classifiers.linear;

import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.FixedProblems;
import com.jstarcraft.ai.jsat.classifiers.*;
import com.jstarcraft.ai.jsat.classifiers.linear.StochasticMultinomialLogisticRegression;
import com.jstarcraft.ai.jsat.exceptions.UntrainedModelException;
import com.jstarcraft.ai.jsat.utils.random.RandomUtil;

import static org.junit.Assert.*;

/**
 *
 * @author Edward Raff
 */
public class StochasticMultinomialLogisticRegressionTest {

    public StochasticMultinomialLogisticRegressionTest() {
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
     * Test of train method, of class StochasticMultinomialLogisticRegression.
     */
    @Test
    public void testTrainC_ClassificationDataSet() {
        System.out.println("trainC");

        ClassificationDataSet train = FixedProblems.get2ClassLinear(400, RandomUtil.getRandom());

        for (StochasticMultinomialLogisticRegression.Prior prior : StochasticMultinomialLogisticRegression.Prior.values()) {

            StochasticMultinomialLogisticRegression smlgr = new StochasticMultinomialLogisticRegression();
            smlgr.setPrior(prior);
            smlgr.train(train);

            ClassificationDataSet test = FixedProblems.get2ClassLinear(400, RandomUtil.getRandom());

            for (DataPointPair<Integer> dpp : test.getAsDPPList())
                assertEquals(dpp.getPair().longValue(), smlgr.classify(dpp.getDataPoint()).mostLikely());
        }
    }

    /**
     * Test of clone method, of class StochasticMultinomialLogisticRegression.
     */
    @Test
    public void testClone() {
        System.out.println("clone");
        StochasticMultinomialLogisticRegression smlgr = new StochasticMultinomialLogisticRegression();

        Classifier cloned = smlgr.clone();

        ClassificationDataSet train = FixedProblems.get2ClassLinear(400, RandomUtil.getRandom());
        cloned.train(train);

        try {
            smlgr.classify(train.getDataPoint(0));
            fail("Exception should have occured");
        } catch (UntrainedModelException ex) {

        }

        train.classSampleCount(train.getDataPointCategory(0));

    }

}
