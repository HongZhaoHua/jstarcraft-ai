package com.jstarcraft.ai.jsat.linear.vectorcollection;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.jsat.classifiers.evaluation.EvaluationTestSuite;
import com.jstarcraft.ai.jsat.clustering.hierarchical.HierarchicalTestSuite;
import com.jstarcraft.ai.jsat.clustering.kmeans.KMeanTestSuite;

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
