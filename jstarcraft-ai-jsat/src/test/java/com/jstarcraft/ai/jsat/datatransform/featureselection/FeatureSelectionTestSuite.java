package com.jstarcraft.ai.jsat.datatransform.featureselection;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        BDSTest.class,

        LRSTest.class,

        MutualInfoFSTest.class,

        ReliefFTest.class,

        SBSTest.class,

        SFSTest.class })
public class FeatureSelectionTestSuite {

}
