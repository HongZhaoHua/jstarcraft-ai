package com.jstarcraft.ai.jsat.distributions.discrete;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        BinomialTest.class,

        PoissonTest.class,

        UniformDiscreteTest.class,

        ZipfTest.class })
public class DiscreteTestSuite {

}
