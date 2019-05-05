package com.jstarcraft.ai.data;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.data.attribute.DataAttributeTestSuite;
import com.jstarcraft.ai.data.converter.DataConverterTestSuite;
import com.jstarcraft.ai.data.module.DataModuleTestSuite;
import com.jstarcraft.ai.data.processor.DataProcessorTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

        DataAttributeTestSuite.class,

        DataConverterTestSuite.class,

        DataModuleTestSuite.class,

        DataProcessorTestSuite.class,

        DataSpaceTestCase.class })
public class DataTestSuite {

}
