package com.jstarcraft.ai.math.algorithm.correlation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.math.algorithm.correlation.similarity.CPCSimilarityTestCase;
import com.jstarcraft.ai.math.algorithm.correlation.similarity.ConsistencyIndexSimilarityTestCase;
import com.jstarcraft.ai.math.algorithm.correlation.similarity.CosineSimilarityTestCase;
import com.jstarcraft.ai.math.algorithm.correlation.similarity.DiceCoefficientSimilarityTestCase;
import com.jstarcraft.ai.math.algorithm.correlation.similarity.JaccardIndexSimilarityTestCase;
import com.jstarcraft.ai.math.algorithm.correlation.similarity.KRCCSimilarityTestCase;
import com.jstarcraft.ai.math.algorithm.correlation.similarity.PCCSimilarityTestCase;
import com.jstarcraft.ai.math.algorithm.correlation.similarity.SpearmanRankCorrelationTestCase;
import com.jstarcraft.ai.math.algorithm.correlation.similarity.TanimotoSimilarityTestCase;

@RunWith(Suite.class)
@SuiteClasses({
        // 相似度测试集
        ConsistencyIndexSimilarityTestCase.class,

        CosineSimilarityTestCase.class,

        CPCSimilarityTestCase.class,

        DiceCoefficientSimilarityTestCase.class,

        JaccardIndexSimilarityTestCase.class,

        KRCCSimilarityTestCase.class,

        PCCSimilarityTestCase.class,

        SpearmanRankCorrelationTestCase.class,

        TanimotoSimilarityTestCase.class })
public class SimilarityTestSuite {

}
