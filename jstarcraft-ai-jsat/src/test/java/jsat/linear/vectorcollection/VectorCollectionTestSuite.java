package jsat.linear.vectorcollection;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jsat.classifiers.evaluation.EvaluationTestSuite;
import jsat.clustering.hierarchical.HierarchicalTestSuite;
import jsat.clustering.kmeans.KMeanTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

        BallTreeTest.class,

        CoverTreeTest.class,

        DCITest.class,

        DualTreeTest.class,

        KDTreeTest.class,

        RandomBallCoverTest.class,

        RTreeTest.class,

        VectorArrayTest.class,

        VPTreeMVTest.class,

        VPTreeTest.class })
public class VectorCollectionTestSuite {

}
