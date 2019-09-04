package jsat.classifiers.trees;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        DecisionStumpTest.class,

        DecisionTreeTest.class,

        ERTreesTest.class,

        ImportanceByUsesTest.class,

        MDATest.class,

        MDITest.class,

        RandomForestTest.class })
public class TreeTestSuite {

}
