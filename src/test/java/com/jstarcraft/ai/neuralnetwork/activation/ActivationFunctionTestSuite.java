package com.jstarcraft.ai.neuralnetwork.activation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		// 激活函数测试集
		CubeActivationFunctionTestCase.class,

		ELUActivationFunctionTestCase.class,

		HardSigmoidActivationFunctionTestCase.class,

		HardTanHActivationFunctionTestCase.class,

		IdentityActivationFunctionTestCase.class,

		LReLUActivationFunctionTestCase.class,

		ReLUActivationFunctionTestCase.class,

		SELUActivationFunctionTestCase.class,

		SigmoidActivationFunctionTestCase.class,

		SoftMaxActivationFunctionTestCase.class,

		SoftPlusActivationFunctionTestCase.class,

		SoftSignActivationFunctionTestCase.class,

		TanHActivationFunctionTestCase.class, })
public class ActivationFunctionTestSuite {

}
