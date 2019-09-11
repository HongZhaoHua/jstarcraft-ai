package com.jstarcraft.ai.data.attribute;

public class MemoryQuantityAttributeTestCase extends QuantityAttributeTestCase {

    @Override
    protected QuantityAttribute<Float> getQuantityAttribute() {
        return new MemoryQuantityAttribute<>("test", Float.class);
    }

}
