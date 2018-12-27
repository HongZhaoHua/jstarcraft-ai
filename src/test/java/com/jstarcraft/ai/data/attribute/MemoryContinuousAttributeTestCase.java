package com.jstarcraft.ai.data.attribute;

public class MemoryContinuousAttributeTestCase extends ContinuousAttributeTestCase {

	@Override
	 protected ContinuousAttribute<Float> getContinuousAttribute() {
	  return new MemoryContinuousAttribute<>("test", Float.class);
	}

}
