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
import com.jstarcraft.ai.jsat.classifiers.linear.ALMA2;
import com.jstarcraft.ai.jsat.utils.random.RandomUtil;

import static org.junit.Assert.*;

/**
 *
 * @author Edward Raff
 */
public class ALMA2Test {

    public ALMA2Test() {
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
     * Test of classify method, of class ALMA2.
     */
    @Test
    public void testTrain_C() {
        System.out.println("classify");

        ClassificationDataSet train = FixedProblems.get2ClassLinear(200, RandomUtil.getRandom());

        ALMA2 alma = new ALMA2();
        alma.setEpochs(1);

        alma.train(train);

        ClassificationDataSet test = FixedProblems.get2ClassLinear(200, RandomUtil.getRandom());

        for (DataPointPair<Integer> dpp : test.getAsDPPList())
            assertEquals(dpp.getPair().longValue(), alma.classify(dpp.getDataPoint()).mostLikely());

    }

}
