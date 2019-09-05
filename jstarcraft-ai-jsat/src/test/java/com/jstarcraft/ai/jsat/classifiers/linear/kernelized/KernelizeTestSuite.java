package com.jstarcraft.ai.jsat.classifiers.linear.kernelized;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        ALMA2KTest.class,

        BOGDTest.class,

        CSKLRBatchTest.class,

        CSKLRTest.class,

        DUOLTest.class,

        ForgetronTest.class,

        KernelPointTest.class,

        KernelSGDTest.class,

        OSKLTest.class,

        ProjectronTest.class })
public class KernelizeTestSuite {

}
