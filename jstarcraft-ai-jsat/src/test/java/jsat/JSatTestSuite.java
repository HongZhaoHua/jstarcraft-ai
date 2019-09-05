package jsat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jsat.classifiers.ClassfierTestSuite;
import jsat.classifiers.linear.LinearTestSuite;
import jsat.clustering.ClustererTestSuite;
import jsat.datatransform.DataTransformTestSuite;
import jsat.distributions.DistributionTestSuite;
import jsat.regression.RegressorTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

        ClassfierTestSuite.class,

        ClustererTestSuite.class,
        
        DataTransformTestSuite.class,
        
        DistributionTestSuite.class,

        LinearTestSuite.class,

        RegressorTestSuite.class })
public class JSatTestSuite {

}
