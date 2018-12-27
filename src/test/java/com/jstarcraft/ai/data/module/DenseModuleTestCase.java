package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.module.DenseModule;

public class DenseModuleTestCase extends DataModuleTestCase {

	@Override
	protected DataModule getDataModule(int discreteOrder, int continuousOrder, int instanceCapacity) {
		return new DenseModule(discreteOrder, continuousOrder, instanceCapacity);
	}

}
