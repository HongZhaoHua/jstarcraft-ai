package com.jstarcraft.ai.jsat.clustering.hierarchical;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        DivisiveGlobalClustererTest.class,

        DivisiveLocalClustererTest.class,

        NNChainHACTest.class,

        PriorityHACTest.class,

        SimpleHACTest.class })
public class HierarchicalTestSuite {

}
