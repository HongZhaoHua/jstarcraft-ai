package com.jstarcraft.ai.jsat.classifiers.svm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.jsat.classifiers.svm.extended.AMMTest;
import com.jstarcraft.ai.jsat.classifiers.svm.extended.CPMTest;

@RunWith(Suite.class)
@SuiteClasses({

        AMMTest.class,

        CPMTest.class,

        DCDsTest.class,

        DCDTest.class,

        DCSVMTest.class,

        LSSVMTest.class,

        PegasosKTest.class,

        PegasosTest.class,

        PlattSMOTest.class,

        SBPTest.class,

        SVMnoBiasTest.class })
public class SvmTestSuite {

}
