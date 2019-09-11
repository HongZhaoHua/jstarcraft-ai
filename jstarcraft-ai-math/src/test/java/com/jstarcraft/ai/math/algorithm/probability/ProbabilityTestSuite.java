package com.jstarcraft.ai.math.algorithm.probability;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        // 概率测试集
        BinomialProbabilityTestCase.class,

        NormalProbabilityTestCase.class,

        UniformProbabilityTestCase.class })
public class ProbabilityTestSuite {

}
