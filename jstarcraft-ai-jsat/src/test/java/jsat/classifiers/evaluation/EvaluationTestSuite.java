package jsat.classifiers.evaluation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        AccuracyTest.class,

        AUCTest.class,

        F1ScoreTest.class,

        FbetaScoreTest.class,

        KappaTest.class,

        LogLossTest.class,

        MatthewsCorrelationCoefficientTest.class,

        PrecisionTest.class,

        RecallTest.class })
public class EvaluationTestSuite {

}
