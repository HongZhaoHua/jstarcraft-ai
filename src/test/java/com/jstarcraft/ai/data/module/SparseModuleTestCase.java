package com.jstarcraft.ai.data.module;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.module.SparseModule;

public class SparseModuleTestCase extends DataModuleTestCase {

	@Override
	protected DataModule getDataModule(int discreteOrder, int continuousOrder, int instanceCapacity) {
		return new SparseModule(discreteOrder, continuousOrder, instanceCapacity);
	}

}
