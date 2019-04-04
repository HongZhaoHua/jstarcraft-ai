package com.jstarcraft.ai.supervised;

import java.util.LinkedList;
import java.util.List;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.module.DenseModule;
import com.jstarcraft.core.utility.KeyValue;

public abstract class PredictorTestCase {

	protected DataModule getDateModule() {
		String name = "module";
		List<KeyValue<KeyValue<String, Boolean>, Integer>> definition = new LinkedList<>();
		for (int index = 0; index < 10; index++) {
			definition.add(new KeyValue<>(new KeyValue<>("user", true), 1));
			definition.add(new KeyValue<>(new KeyValue<>("item", true), 1));
			definition.add(new KeyValue<>(new KeyValue<>("score", false), 1));
		}
		DenseModule module = new DenseModule(name, definition, 20);
		return module;
	}

}
