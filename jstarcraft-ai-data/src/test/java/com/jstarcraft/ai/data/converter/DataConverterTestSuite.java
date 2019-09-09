package com.jstarcraft.ai.data.converter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        ArffConverterTestCase.class,

        CsvConverterTestCase.class,

        JsonConverterTestCase.class,

        ParquetConverterTestCase.class,

        QueryConverterTestCase.class })
public class DataConverterTestSuite {

}
