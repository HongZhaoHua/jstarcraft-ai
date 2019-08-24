package com.jstarcraft.ai.math.algorithm.correlation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        // 相似度测试集
        BinarySimilarityTestCase.class,

        CosineSimilarityTestCase.class,

        CPCSimilarityTestCase.class,

        DiceCoefficientSimilarityTestCase.class,

        EuclideanDistanceSimilarityTestCase.class,

        JaccardSimilarityTestCase.class,

        KRCCSimilarityTestCase.class,

        LevensteinDistanceTestCase.class,

        ManhattanDistanceSimilarityTestCase.class,

        MSDSimilarityTestCase.class,

        MSESimilarityTestCase.class,

        PCCSimilarityTestCase.class,

        TanimotoSimilarityTestCase.class })
public class SimilarityTestSuite {

}
