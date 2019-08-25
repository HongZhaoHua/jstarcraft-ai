package com.jstarcraft.ai.math.algorithm.correlation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        // 距离测试集
        EuclideanDistanceTestCase.class,

        LevensteinDistanceTestCase.class,

        ManhattanDistanceTestCase.class,

        MSDDistanceTestCase.class,

        MSEDistanceTestCase.class })
public class DistanceTestSuite {

}
