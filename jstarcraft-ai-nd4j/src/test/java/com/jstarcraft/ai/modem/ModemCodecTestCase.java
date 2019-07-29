package com.jstarcraft.ai.modem;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.modem.ModemCodec;

public class ModemCodecTestCase {

	@Test
	public void test() {
		for (ModemCodec codec : ModemCodec.values()) {
			Map<String, Object> oldModel = new HashMap<>();
			oldModel.put("complex", MockComplexObject.valueOf(0, "birdy", "hong", 10, Instant.now(), MockEnumeration.TERRAN));
			oldModel.put("enumeration", MockEnumeration.TERRAN);
			oldModel.put("simple", MockSimpleObject.valueOf(0, "mickey"));
			Assert.assertTrue(MockComplexObject.class.cast(oldModel.get("complex")).toNames() != null);
			Assert.assertTrue(MockComplexObject.class.cast(oldModel.get("complex")).toCurrencies() != null);
			byte[] data = codec.encodeModel(oldModel);
			Assert.assertTrue(MockComplexObject.class.cast(oldModel.get("complex")).toNames() == null);
			Assert.assertTrue(MockComplexObject.class.cast(oldModel.get("complex")).toCurrencies() == null);
			Map<String, Object> newModel = (Map) codec.decodeModel(data);
			Assert.assertTrue(MockComplexObject.class.cast(newModel.get("complex")).toNames() != null);
			Assert.assertTrue(MockComplexObject.class.cast(newModel.get("complex")).toCurrencies() != null);
			Assert.assertThat(newModel, CoreMatchers.equalTo(oldModel));
		}
	}

}
