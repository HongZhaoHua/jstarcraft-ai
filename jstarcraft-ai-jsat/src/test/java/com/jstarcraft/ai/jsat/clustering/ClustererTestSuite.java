package com.jstarcraft.ai.jsat.clustering;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.jsat.classifiers.evaluation.EvaluationTestSuite;
import com.jstarcraft.ai.jsat.clustering.hierarchical.HierarchicalTestSuite;
import com.jstarcraft.ai.jsat.clustering.kmeans.KMeanTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

        EvaluationTestSuite.class,

        HierarchicalTestSuite.class,

        KMeanTestSuite.class,

        CLARATest.class,

        DBSCANTest.class,

        EMGaussianMixtureTest.class,

        FLAMETest.class,

        GapStatisticTest.class,

        HDBSCANTest.class,

        LSDBCTest.class,

        MeanShiftTest.class,

        MEDDITTest.class,

        OPTICSTest.class,

        PAMTest.class,

        TRIKMEDSTest.class })
public class ClustererTestSuite {

}
