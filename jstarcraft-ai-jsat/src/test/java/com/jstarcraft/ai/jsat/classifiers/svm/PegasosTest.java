/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jstarcraft.ai.jsat.classifiers.svm;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.FixedProblems;
import com.jstarcraft.ai.jsat.classifiers.ClassificationDataSet;
import com.jstarcraft.ai.jsat.classifiers.Classifier;
import com.jstarcraft.ai.jsat.classifiers.DataPointPair;
import com.jstarcraft.ai.jsat.classifiers.svm.Pegasos;
import com.jstarcraft.ai.jsat.datatransform.DataModelPipeline;
import com.jstarcraft.ai.jsat.datatransform.PNormNormalization;
import com.jstarcraft.ai.jsat.utils.random.RandomUtil;

/**
 *
 * @author Edward Raff
 */
public class PegasosTest {

    public PegasosTest() {
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
     * Test of train method, of class Pegasos.
     */
    @Test
    public void testTrainC_ClassificationDataSet_ExecutorService() {
        System.out.println("trainC");
        ClassificationDataSet train = FixedProblems.get2ClassLinear(2000, RandomUtil.getRandom());

        for (boolean parallel : new boolean[] { true, false }) {
            Classifier instance = new DataModelPipeline(new Pegasos(), new PNormNormalization());
            instance.train(train, parallel);

            ClassificationDataSet test = FixedProblems.get2ClassLinear(200, RandomUtil.getRandom());

            for (DataPointPair<Integer> dpp : test.getAsDPPList())
                assertEquals(dpp.getPair().longValue(), instance.classify(dpp.getDataPoint()).mostLikely());
        }
    }

}
