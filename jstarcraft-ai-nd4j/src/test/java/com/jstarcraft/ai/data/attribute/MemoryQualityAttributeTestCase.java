package com.jstarcraft.ai.data.attribute;

public class MemoryQualityAttributeTestCase extends QualityAttributeTestCase {

	@Override
	 protected QualityAttribute<Float> getQualityAttribute() {
	  return new MemoryQualityAttribute<>("test", Float.class);
	}

}
