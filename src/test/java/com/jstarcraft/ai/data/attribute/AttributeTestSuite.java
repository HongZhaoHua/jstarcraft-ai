package com.jstarcraft.ai.data.attribute;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

		MemoryContinuousAttributeTestCase.class,

		MemoryDiscreteAttributeTestCase.class,

		RedisContinuousAttributeTestCase.class,

		RedisDiscreteAttributeTestCase.class })
public class AttributeTestSuite {

}
