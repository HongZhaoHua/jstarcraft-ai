package jsat.classifiers.linear;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        ALMA2Test.class,

        AROWTest.class,

        BBRTest.class,

        LinearBatchTest.class,

        LinearL1SCDTest.class,

        LinearSGDTest.class,

        LogisticRegressionDCDTest.class,

        NewGLMNETTest.class,

        NHERDTest.class,

        PassiveAggressiveTest.class,

        ROMMATest.class,

        SCDTest.class,

        SCWTest.class,

        SDCATest.class,

        SMIDASTest.class,

        STGDTest.class,

        StochasticMultinomialLogisticRegressionTest.class })
public class LinearTestSuite {

}
