package com.jstarcraft.ai.neuralnetwork.learn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		// 学习器测试集
		AdaDeltaLearnerTestCase.class,

		AdaGradLearnerTestCase.class,

		AdaMaxLearnerTestCase.class,

		AdamLearnerTestCase.class,

		IgnoreLearnerTestCase.class,

		NadamLearnerTestCase.class,

		NesterovLearnerTestCase.class,

		RmsPropLearnerTestCase.class,

		SgdLearnerTestCase.class, })
public class LearnerTestSuite {

}
