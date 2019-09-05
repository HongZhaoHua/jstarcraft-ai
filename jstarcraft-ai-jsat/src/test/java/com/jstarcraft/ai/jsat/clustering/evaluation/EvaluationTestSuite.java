package com.jstarcraft.ai.jsat.clustering.evaluation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.jsat.clustering.evaluation.intra.SumOfSqrdPairwiseDistancesTest;

@RunWith(Suite.class)
@SuiteClasses({

        SumOfSqrdPairwiseDistancesTest.class,

        AdjustedRandIndexTest.class,

        CompletenessTest.class,

        HomogeneityTest.class,

        NormalizedMutualInformationTest.class,

        VMeasureTest.class })
public class EvaluationTestSuite {

}
