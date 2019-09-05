
package com.jstarcraft.ai.jsat.classifiers.boosting;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.FixedProblems;
import com.jstarcraft.ai.jsat.classifiers.ClassificationDataSet;
import com.jstarcraft.ai.jsat.classifiers.DataPointPair;
import com.jstarcraft.ai.jsat.classifiers.OneVSAll;
import com.jstarcraft.ai.jsat.classifiers.boosting.Stacking;
import com.jstarcraft.ai.jsat.classifiers.linear.LinearBatch;
import com.jstarcraft.ai.jsat.classifiers.linear.LogisticRegressionDCD;
import com.jstarcraft.ai.jsat.classifiers.svm.DCDs;
import com.jstarcraft.ai.jsat.lossfunctions.SoftmaxLoss;
import com.jstarcraft.ai.jsat.regression.RegressionDataSet;
import com.jstarcraft.ai.jsat.regression.Regressor;
import com.jstarcraft.ai.jsat.utils.random.RandomUtil;

/**
 *
 * @author Edward Raff
 */
public class StackingTest {
    public StackingTest() {
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
    public void testClassifyBinary() {
        System.out.println("binary classifiation");

        Stacking stacking = new Stacking(new LogisticRegressionDCD(), new LinearBatch(new SoftmaxLoss(), 1e-15), new LinearBatch(new SoftmaxLoss(), 100), new LinearBatch(new SoftmaxLoss(), 1e10));

        ClassificationDataSet train = FixedProblems.get2ClassLinear(500, RandomUtil.getRandom());

        stacking = stacking.clone();
        stacking.train(train);
        stacking = stacking.clone();

        ClassificationDataSet test = FixedProblems.get2ClassLinear(200, RandomUtil.getRandom());

        for (DataPointPair<Integer> dpp : test.getAsDPPList())
            assertEquals(dpp.getPair().longValue(), stacking.classify(dpp.getDataPoint()).mostLikely());
    }

    @Test
    public void testClassifyBinaryMT() {
        System.out.println("binary classifiation MT");

        Stacking stacking = new Stacking(new LogisticRegressionDCD(), new LinearBatch(new SoftmaxLoss(), 1e-15), new LinearBatch(new SoftmaxLoss(), 100), new LinearBatch(new SoftmaxLoss(), 1e10));

        ClassificationDataSet train = FixedProblems.get2ClassLinear(500, RandomUtil.getRandom());

        stacking = stacking.clone();
        stacking.train(train, true);
        stacking = stacking.clone();

        ClassificationDataSet test = FixedProblems.get2ClassLinear(200, RandomUtil.getRandom());

        for (DataPointPair<Integer> dpp : test.getAsDPPList())
            assertEquals(dpp.getPair().longValue(), stacking.classify(dpp.getDataPoint()).mostLikely());
    }

    @Test
    public void testClassifyMulti() {
        Stacking stacking = new Stacking(new OneVSAll(new LogisticRegressionDCD(), true), new LinearBatch(new SoftmaxLoss(), 1e-15), new LinearBatch(new SoftmaxLoss(), 100), new LinearBatch(new SoftmaxLoss(), 1e10));

        ClassificationDataSet train = FixedProblems.getSimpleKClassLinear(500, 6, RandomUtil.getRandom());

        stacking = stacking.clone();
        stacking.train(train);
        stacking = stacking.clone();

        ClassificationDataSet test = FixedProblems.getSimpleKClassLinear(200, 6, RandomUtil.getRandom());

        for (DataPointPair<Integer> dpp : test.getAsDPPList())
            assertEquals(dpp.getPair().longValue(), stacking.classify(dpp.getDataPoint()).mostLikely());
    }

    @Test
    public void testClassifyMultiMT() {
        System.out.println("multi class classification MT");

        Stacking stacking = new Stacking(new OneVSAll(new LogisticRegressionDCD(), true), new LinearBatch(new SoftmaxLoss(), 1e-15), new LinearBatch(new SoftmaxLoss(), 100), new LinearBatch(new SoftmaxLoss(), 1e10));

        ClassificationDataSet train = FixedProblems.getSimpleKClassLinear(500, 6, RandomUtil.getRandom());

        stacking = stacking.clone();
        stacking.train(train, true);
        stacking = stacking.clone();

        ClassificationDataSet test = FixedProblems.getSimpleKClassLinear(200, 6, RandomUtil.getRandom());

        for (DataPointPair<Integer> dpp : test.getAsDPPList())
            assertEquals(dpp.getPair().longValue(), stacking.classify(dpp.getDataPoint()).mostLikely());
    }

    @Test
    public void testRegression() {
        System.out.println("regression");

        Stacking stacking = new Stacking((Regressor) new DCDs(1000, true), new DCDs(1000, false), new DCDs(1000, 1e-3, 0.5, true));
        RegressionDataSet train = FixedProblems.getLinearRegression(500, RandomUtil.getRandom());

        stacking = stacking.clone();
        stacking.train(train);
        stacking = stacking.clone();

        RegressionDataSet test = FixedProblems.getLinearRegression(200, RandomUtil.getRandom());

        for (DataPointPair<Double> dpp : test.getAsDPPList()) {
            double truth = dpp.getPair();
            double pred = stacking.regress(dpp.getDataPoint());
            double relErr = (truth - pred) / truth;
            assertEquals(0, relErr, 0.1);
        }
    }

    // TODO 暂时屏蔽此测试
    // @Test
    public void testRegressionMT() {
        System.out.println("regression MT");

        Stacking stacking = new Stacking((Regressor) new DCDs(1000, true), new DCDs(1000, false), new DCDs(1000, 1e-3, 0.5, true));
        RegressionDataSet train = FixedProblems.get2DLinearRegression(500, RandomUtil.getRandom());

        stacking = stacking.clone();
        stacking.train(train, true);
        stacking = stacking.clone();

        RegressionDataSet test = FixedProblems.get2DLinearRegression(200, RandomUtil.getRandom());

        for (DataPointPair<Double> dpp : test.getAsDPPList()) {
            double truth = dpp.getPair();
            double pred = stacking.regress(dpp.getDataPoint());
            double relErr = (truth - pred) / truth;
            assertEquals(0, relErr, 0.1);
        }
    }

}
