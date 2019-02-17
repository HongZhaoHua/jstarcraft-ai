package com.jstarcraft.ai.data.converter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

		CsvConverterTestCase.class,

		HibernateConverterTestCase.class,

		JdbcConverterTestCase.class,

		JsonConverterTestCase.class })
public class DataConverterTestSuite {

}
