package com.jstarcraft.ai.jsat.distributions.multivariate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.jsat.clustering.kmeans.KMeanTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

        DirichletTest.class,

        NormalMTest.class,

        KMeanTestSuite.class,

        SymmetricDirichletTest.class })
public class MultivariateTestSuite {

}
