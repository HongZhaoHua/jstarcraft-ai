package com.jstarcraft.ai.jsat.regression;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        AveragedRegressorTest.class,

        KernelRidgeRegressionTest.class,

        KernelRLSTest.class,

        NadarayaWatsonTest.class,

        OrdinaryKrigingTest.class,

        RANSACTest.class,

        RidgeRegressionTest.class,

        StochasticGradientBoostingTest.class,

        StochasticRidgeRegressionTest.class })
public class RegressorTestSuite {

}
