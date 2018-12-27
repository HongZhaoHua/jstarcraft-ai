package com.jstarcraft.ai.data.attribute;

public class MemoryDiscreteAttributeTestCase extends DiscreteAttributeTestCase {

	@Override
	 protected DiscreteAttribute<Float> getDiscreteAttribute() {
	  return new MemoryDiscreteAttribute<>("test", Float.class);
	}

}
