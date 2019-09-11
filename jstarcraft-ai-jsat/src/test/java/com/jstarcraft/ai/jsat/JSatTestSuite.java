package com.jstarcraft.ai.jsat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.jsat.classifiers.ClassfierTestSuite;
import com.jstarcraft.ai.jsat.classifiers.linear.LinearTestSuite;
import com.jstarcraft.ai.jsat.clustering.ClustererTestSuite;
import com.jstarcraft.ai.jsat.datatransform.DataTransformTestSuite;
import com.jstarcraft.ai.jsat.distributions.DistributionTestSuite;
import com.jstarcraft.ai.jsat.regression.RegressorTestSuite;

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
