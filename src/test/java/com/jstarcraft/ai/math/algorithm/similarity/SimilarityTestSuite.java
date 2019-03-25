package com.jstarcraft.ai.math.algorithm.similarity;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		// 相似度测试集
		BinaryCosineSimilarityTestCase.class,

		CosineSimilarityTestCase.class,

		CPCSimilarityTestCase.class,

		DiceCoefficientSimilarityTestCase.class,

		TanimotoSimilarityTestCase.class,

		JaccardSimilarityTestCase.class,

		KRCCSimilarityTestCase.class,

		MSDSimilarityTestCase.class,

		MSESimilarityTestCase.class,

		PCCSimilarityTestCase.class })
public class SimilarityTestSuite {

}
