package com.jstarcraft.ai.data.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.DataSpace;

public class CsvConverterTestCase {

	@Test
	public void testConvert() throws Exception {
		Map<String, Class<?>> qualityDifinitions = new HashMap<>();
		Map<String, Class<?>> quantityDifinitions = new HashMap<>();
		qualityDifinitions.put("user", int.class);
		qualityDifinitions.put("item", int.class);
		quantityDifinitions.put("score", float.class);
		DataSpace space = new DataSpace(qualityDifinitions, quantityDifinitions);

		TreeMap<Integer, String> configuration = new TreeMap<>();
		configuration.put(2, "user");
		configuration.put(4, "item");
		configuration.put(5, "score");

		CsvConverter converter = new CsvConverter(',', space.getQualityAttributes(), space.getQuantityAttributes());
		{
			DataModule dense = space.makeDenseModule("dense", configuration, 1000);
			File file = new File(this.getClass().getResource("dense.csv").toURI());
			InputStream stream = new FileInputStream(file);
			int count = converter.convert(dense, stream);
			Assert.assertEquals(5, count);
		}
		{
			DataModule sparse = space.makeSparseModule("sparse", configuration, 1000);
			File file = new File(this.getClass().getResource("sparse.csv").toURI());
			InputStream stream = new FileInputStream(file);
			int count = converter.convert(sparse, stream);
			Assert.assertEquals(5, count);
		}
	}

}
