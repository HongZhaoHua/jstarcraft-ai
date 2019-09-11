package com.jstarcraft.ai.data.module;

import java.util.List;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.core.utility.KeyValue;

public class DenseModuleTestCase extends DataModuleTestCase {

    @Override
    protected DataModule getDataModule(String moduleName, List<KeyValue<KeyValue<String, Boolean>, Integer>> moduleDefinition, int instanceCapacity) {
        return new DenseModule(moduleName, moduleDefinition, instanceCapacity);
    }

}
