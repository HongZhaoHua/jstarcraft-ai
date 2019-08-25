package com.jstarcraft.ai.math.algorithm.correlation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        // 距离测试集
        DistanceTestSuite.class,

        // 相似度测试集
        SimilarityTestSuite.class })
public class CorrelationTestSuite {

}
