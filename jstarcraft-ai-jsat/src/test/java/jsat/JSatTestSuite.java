package jsat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jsat.classifiers.ClassfierTestSuite;
import jsat.classifiers.linear.LinearTestSuite;
import jsat.clustering.ClustererTestSuite;
import jsat.regression.RegressorTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

        ClassfierTestSuite.class,

        ClustererTestSuite.class,

        LinearTestSuite.class,

        RegressorTestSuite.class })
public class JSatTestSuite {

}
