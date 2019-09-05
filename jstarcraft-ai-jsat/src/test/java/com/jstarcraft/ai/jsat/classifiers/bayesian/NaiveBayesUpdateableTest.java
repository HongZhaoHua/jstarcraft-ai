/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jstarcraft.ai.jsat.classifiers.bayesian;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.classifiers.ClassificationDataSet;
import com.jstarcraft.ai.jsat.classifiers.Classifier;
import com.jstarcraft.ai.jsat.classifiers.bayesian.NaiveBayesUpdateable;
import com.jstarcraft.ai.jsat.distributions.Normal;
import com.jstarcraft.ai.jsat.utils.GridDataGenerator;

/**
 *
 * @author Edward Raff
 */
public class NaiveBayesUpdateableTest {
    static private ClassificationDataSet easyTrain;
    static private ClassificationDataSet easyTest;
    static private NaiveBayesUpdateable nb;

    public NaiveBayesUpdateableTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        GridDataGenerator gdg = new GridDataGenerator(new Normal(0, 0.05), new Random(12), 2);
        easyTrain = new ClassificationDataSet(gdg.generateData(40).getList(), 0);
        easyTest = new ClassificationDataSet(gdg.generateData(40).getList(), 0);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        nb = new NaiveBayesUpdateable();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTrainC_ClassificationDataSet() {
        System.out.println("trainC");
        nb.train(easyTrain);
        for (int i = 0; i < easyTest.size(); i++)
            assertEquals(easyTest.getDataPointCategory(i), nb.classify(easyTest.getDataPoint(i)).mostLikely());
    }

    @Test
    public void testSetUpUpdate() {
        System.out.println("testSetUpUpdate");
        nb.setUp(easyTrain.getCategories(), easyTrain.getNumNumericalVars(), easyTrain.getPredicting());
        for (int i = 0; i < easyTrain.size(); i++)
            nb.update(easyTrain.getDataPoint(i), easyTrain.getDataPointCategory(i));
        for (int i = 0; i < easyTest.size(); i++)
            assertEquals(easyTest.getDataPointCategory(i), nb.classify(easyTest.getDataPoint(i)).mostLikely());
    }

    @Test
    public void testClone() {
        System.out.println("clone");
        nb.train(easyTrain);
        Classifier clone = nb.clone();
        for (int i = 0; i < easyTest.size(); i++)
            assertEquals(easyTest.getDataPointCategory(i), clone.classify(easyTest.getDataPoint(i)).mostLikely());
    }

    @Test
    public void testTrainC_ClassificationDataSet_ExecutorService() {
        System.out.println("trainC");
        nb.train(easyTrain, true);
        for (int i = 0; i < easyTest.size(); i++)
            assertEquals(easyTest.getDataPointCategory(i), nb.classify(easyTest.getDataPoint(i)).mostLikely());
    }
}
