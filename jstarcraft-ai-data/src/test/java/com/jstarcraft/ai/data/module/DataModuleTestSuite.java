package com.jstarcraft.ai.data.module;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        DenseModuleTestCase.class,

        SparseModuleTestCase.class,

        ReferenceModuleTestCase.class })
public class DataModuleTestSuite {

}
