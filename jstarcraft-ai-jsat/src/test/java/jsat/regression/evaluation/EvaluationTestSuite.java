package jsat.regression.evaluation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        CoefficientOfDeterminationTest.class,

        MeanAbsoluteErrorTest.class,

        MeanSquaredErrorTest.class,

        RelativeAbsoluteErrorTest.class,

        RelativeSquaredErrorTest.class })
public class EvaluationTestSuite {

}
