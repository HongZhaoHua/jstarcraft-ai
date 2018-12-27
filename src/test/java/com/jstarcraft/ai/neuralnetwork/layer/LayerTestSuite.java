package com.jstarcraft.ai.neuralnetwork.layer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		// 层测试集
		EmbedLayerTestCase.class,

		WeightLayerTestCase.class, })
public class LayerTestSuite {

}
