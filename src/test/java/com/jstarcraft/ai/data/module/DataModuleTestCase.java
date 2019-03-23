package com.jstarcraft.ai.data.module;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.exception.DataCapacityException;
import com.jstarcraft.core.utility.KeyValue;
import com.jstarcraft.core.utility.RandomUtility;

import it.unimi.dsi.fastutil.ints.Int2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatSortedMap;
import it.unimi.dsi.fastutil.ints.Int2IntAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;

public abstract class DataModuleTestCase {

	protected abstract DataModule getDataModule(String moduleName, List<KeyValue<KeyValue<String, Boolean>, Integer>> moduleDefinition, int instanceCapacity);

	@Test
	public void testInnerWithOuter() {
		String moduleName = "module";
		List<KeyValue<KeyValue<String, Boolean>, Integer>> moduleDefinition = new LinkedList<>();
		for (int index = 0; index < 10; index++) {
			moduleDefinition.add(new KeyValue<>(new KeyValue<>("user", true), 1));
			moduleDefinition.add(new KeyValue<>(new KeyValue<>("item", true), 1));
			moduleDefinition.add(new KeyValue<>(new KeyValue<>("score", false), 1));
		}
		int qualityOrder = 10, continuousOrder = 10, instanceCapacity = 10;
		DataModule module = getDataModule(moduleName, moduleDefinition, instanceCapacity);
		Assert.assertThat(module.getQualityInner("user"), CoreMatchers.equalTo(10));
		Assert.assertThat(module.getQualityInner("item"), CoreMatchers.equalTo(0));
		Assert.assertThat(module.getContinuousInner("score"), CoreMatchers.equalTo(0));
		for (int index = 0; index < 10;) {
			{
				Entry<Integer, KeyValue<String, Boolean>> term = module.getOuterKeyValue(index);
				Assert.assertThat(term.getKey(), CoreMatchers.equalTo(index));
				index++;
			}
			{
				Entry<Integer, KeyValue<String, Boolean>> term = module.getOuterKeyValue(index);
				Assert.assertThat(term.getKey(), CoreMatchers.equalTo(index));
				index++;
			}
		}
	}

	@Test
	public void testAssociateInstance() {
		String moduleName = "module";
		List<KeyValue<KeyValue<String, Boolean>, Integer>> moduleDefinition = new LinkedList<>();
		for (int index = 0; index < 10; index++) {
			moduleDefinition.add(new KeyValue<>(new KeyValue<>("quality", true), 1));
			moduleDefinition.add(new KeyValue<>(new KeyValue<>("ontinuous", false), 1));
		}
		int qualityOrder = 10, continuousOrder = 10, instanceCapacity = 10;
		DataModule module = getDataModule(moduleName, moduleDefinition, instanceCapacity);
		Assert.assertEquals(0, module.getSize());

		Int2IntSortedMap qualityFeatures = new Int2IntAVLTreeMap();
		for (int index = 0; index < qualityOrder; index++) {
			qualityFeatures.put(index, RandomUtility.randomInteger(1));
		}
		Int2FloatSortedMap continuousFeatures = new Int2FloatAVLTreeMap();
		for (int index = 0; index < continuousOrder; index++) {
			continuousFeatures.put(index, RandomUtility.randomFloat(1F));
		}
		for (int index = 0; index < instanceCapacity; index++) {
			module.associateInstance(qualityFeatures, continuousFeatures);
			Assert.assertEquals(index + 1, module.getSize());
			DataInstance instance = module.getInstance(index);
			for (Int2IntMap.Entry term : qualityFeatures.int2IntEntrySet()) {
				Assert.assertEquals(term.getIntValue(), instance.getQualityFeature(term.getIntKey()), 0F);
			}
			for (Int2FloatMap.Entry term : continuousFeatures.int2FloatEntrySet()) {
				Assert.assertEquals(term.getFloatValue(), instance.getQuantityFeature(term.getIntKey()), 0F);
			}
		}

		try {
			module.associateInstance(qualityFeatures, continuousFeatures);
			Assert.fail();
		} catch (DataCapacityException exception) {
		}
	}

}
