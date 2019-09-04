package jsat.clustering;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jsat.classifiers.evaluation.EvaluationTestSuite;
import jsat.clustering.hierarchical.HierarchicalTestSuite;
import jsat.clustering.kmeans.KMeanTestSuite;

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
