package com.jstarcraft.ai.data;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.data.converter.DataConverterTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

		DataConverterTestSuite.class,

		DataSpaceTestCase.class })
public class DataTestSuite {

}
