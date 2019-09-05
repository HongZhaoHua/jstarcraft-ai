package jsat.distributions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jsat.distributions.discrete.DiscreteTestSuite;
import jsat.distributions.empirical.EmpiricalTestSuite;
import jsat.distributions.multivariate.MultivariateTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

        DiscreteTestSuite.class,

        EmpiricalTestSuite.class,

        MultivariateTestSuite.class,

        BetaTest.class,

        CauchyTest.class,

        ChiSquaredTest.class,

        ContinuousDistributionTest.class,

        ExponentialTest.class,

        FisherSendorTest.class,

        GammaTest.class,

        KolmogorovTest.class,

        KumaraswamyTest.class,

        LaplaceTest.class,

        LevyTest.class,

        LogisticTest.class,

        LogNormalTest.class,

        LogUniformTest.class,

        MaxwellBoltzmannTest.class,

        NormalTest.class,

        ParetoTest.class,

        RayleighTest.class,

        StudentTTest.class,

        TruncatedDistributionTest.class,

        UniformTest.class,

        WeibullTest.class })
public class DistributionTestSuite {

}
