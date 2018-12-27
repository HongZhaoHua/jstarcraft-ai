package com.jstarcraft.ai.data.attribute;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.config.Config;

public class RedisContinuousAttributeTestCase extends ContinuousAttributeTestCase {

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
	protected ContinuousAttribute<Float> getContinuousAttribute() {
		return new RedisContinuousAttribute<>("test", Float.class, redisson);
	}

}
