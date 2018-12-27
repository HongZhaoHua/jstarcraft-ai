package com.jstarcraft.ai.data.attribute;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.config.Config;

public class RedisDiscreteAttributeTestCase extends DiscreteAttributeTestCase {

	private static Redisson redisson;

	@BeforeClass
	public static void beforeClass() {
		Config configuration = new Config();
		configuration.useSingleServer().setAddress("redis://127.0.0.1:6379");
		redisson = (Redisson) Redisson.create(configuration);
	}

	@AfterClass
	public static void afterClass() {
		redisson.shutdown();
	}

	@Before
	public void beforeTest() {
		RKeys keys = redisson.getKeys();
		keys.flushdb();
	}

	@After
	public void afterTest() {
		RKeys keys = redisson.getKeys();
		keys.flushdb();
	}

	@Override
	protected DiscreteAttribute<Float> getDiscreteAttribute() {
		return new RedisDiscreteAttribute<>("test", Float.class, 1, 10, redisson);
	}

}
