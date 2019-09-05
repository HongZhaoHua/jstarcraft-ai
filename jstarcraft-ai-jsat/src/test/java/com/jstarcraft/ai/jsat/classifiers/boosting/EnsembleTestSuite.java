package com.jstarcraft.ai.jsat.classifiers.boosting;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        AdaBoostM1Test.class,

        ArcX4Test.class,

        BaggingTest.class,

        EmphasisBoostTest.class,

        LogitBoostPLTest.class,

        LogitBoostTest.class,

        ModestAdaBoostTest.class,

        SAMMETest.class,

        StackingTest.class,

        UpdatableStackingTest.class,

        WaggingNormalTest.class })
public class EnsembleTestSuite {

}
