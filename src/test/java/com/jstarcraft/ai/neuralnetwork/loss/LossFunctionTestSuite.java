package com.jstarcraft.ai.neuralnetwork.loss;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		// 损失函数测试集
		BinaryXENTLossFunctionTestCase.class,

		CosineProximityLossFunctionTestCase.class,

		FMeasureLossFunctionTestCase.class,

		HingeLossFunctionTestCase.class,

		KLDLossFunctionTestCase.class,

		L1LossFunctionTestCase.class,

		L2LossFunctionTestCase.class,

		MAELossFunctionTestCase.class,

		MAPELossFunctionTestCase.class,

		MCXENTLossFunctionTestCase.class,

		MixtureDensityLossFunctionTestCase.class,

		MSELossFunctionTestCase.class,

		MSLELossFunctionTestCase.class,

		PoissonLossFunctionTestCase.class,

		SquaredHingeLossFunctionTestCase.class, })
public class LossFunctionTestSuite {

}
