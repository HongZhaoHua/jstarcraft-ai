package com.jstarcraft.ai.data;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.attribute.ContinuousAttribute;
import com.jstarcraft.ai.data.attribute.DiscreteAttribute;

public class DataSpaceTestCase {

	@Test
	public void test() {
		Map<String, Class<?>> discreteDifinitions = new HashMap<>();
		Map<String, Class<?>> continuousDifinitions = new HashMap<>();
		discreteDifinitions.put("user", int.class);
		discreteDifinitions.put("item", int.class);
		continuousDifinitions.put("score", float.class);
		DataSpace space = new DataSpace(discreteDifinitions, continuousDifinitions);

		// 获取数据属性
		DiscreteAttribute userAttribute = space.getDiscreteAttribute("user");
		Assert.assertNotNull(userAttribute);
		DiscreteAttribute itemAttribute = space.getDiscreteAttribute("item");
		Assert.assertNotNull(itemAttribute);
		ContinuousAttribute scoreAttribute = space.getContinuousAttribute("score");
		Assert.assertNotNull(scoreAttribute);

		// 制造数据模型
		{
			TreeMap<Integer, String> configuration = new TreeMap<>();
			configuration.put(1, "user");
			configuration.put(2, "item");
			configuration.put(3, "score");
			DataModule sparseModel = space.makeSparseModule("sparse", configuration, 1000);
			Assert.assertEquals(2, sparseModel.getDiscreteOrder());
			Assert.assertEquals(1, sparseModel.getContinuousOrder());
		}

		{
			TreeMap<Integer, String> configuration = new TreeMap<>();
			configuration.put(2, "user");
			DataModule denseModel = space.makeDenseModule("dense", configuration, 1000);
			Assert.assertEquals(2, denseModel.getDiscreteOrder());
			Assert.assertEquals(0, denseModel.getContinuousOrder());
		}
	}

}
