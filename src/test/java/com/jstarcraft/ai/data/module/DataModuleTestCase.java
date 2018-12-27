package com.jstarcraft.ai.data.module;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.exception.DataCapacityException;
import com.jstarcraft.core.utility.RandomUtility;

import it.unimi.dsi.fastutil.ints.Int2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatSortedMap;
import it.unimi.dsi.fastutil.ints.Int2IntAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;

public abstract class DataModuleTestCase {
	
	protected abstract DataModule getDataModule(int discreteOrder, int continuousOrder, int instanceCapacity);

	@Test
	public void testAssociateInstance() {
		int discreteOrder = 5, continuousOrder = 5, instanceCapacity = 10;
		Int2IntSortedMap discreteFeatures = new Int2IntAVLTreeMap();
		for (int index = 0; index < discreteOrder; index++) {
			discreteFeatures.put(index, RandomUtility.randomInteger(1));
		}
		Int2FloatSortedMap continuousFeatures = new Int2FloatAVLTreeMap();
		for (int index = 0; index < continuousOrder; index++) {
			continuousFeatures.put(index, RandomUtility.randomFloat(1F));
		}

		DataModule module = getDataModule(discreteOrder, continuousOrder, instanceCapacity);
		Assert.assertEquals(0, module.getSize());

		for (int index = 0; index < instanceCapacity; index++) {
			module.associateInstance(discreteFeatures, continuousFeatures);
			Assert.assertEquals(index + 1, module.getSize());
			DataInstance instance = module.getInstance(index);
			for (Int2IntMap.Entry term : discreteFeatures.int2IntEntrySet()) {
				Assert.assertEquals(term.getIntValue(), instance.getDiscreteFeature(term.getIntKey()), 0F);
			}
			for (Int2FloatMap.Entry term : continuousFeatures.int2FloatEntrySet()) {
				Assert.assertEquals(term.getFloatValue(), instance.getContinuousFeature(term.getIntKey()), 0F);
			}
		}

		try {
			module.associateInstance(discreteFeatures, continuousFeatures);
			Assert.fail();
		} catch (DataCapacityException exception) {
		}
	}

}
