package com.jstarcraft.ai.jsat.classifiers.bayesian;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        AODETest.class,

        MultinomialNaiveBayesTest.class,

        MultivariateNormalsTest.class,

        NaiveBayesTest.class,

        NaiveBayesUpdateableTest.class })
public class BayesianTestSuite {

}
