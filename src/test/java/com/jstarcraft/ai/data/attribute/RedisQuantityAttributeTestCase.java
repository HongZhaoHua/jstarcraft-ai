package com.jstarcraft.ai.data.attribute;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.config.Config;

public class RedisQuantityAttributeTestCase extends QuantityAttributeTestCase {

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
	protected QuantityAttribute<Float> getQuantityAttribute() {
		return new RedisQuantityAttribute<>("test", Float.class, redisson);
	}

}
