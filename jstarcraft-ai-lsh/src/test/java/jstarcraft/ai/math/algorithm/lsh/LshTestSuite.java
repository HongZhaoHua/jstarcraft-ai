package jstarcraft.ai.math.algorithm.lsh;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        LshTestCase.class,

        CosineHashTestCase.class,

        EuclideanHashTestCase.class,

        ManhattanHashTestCase.class,

        MinHashTestCase.class, })
public class LshTestSuite {

}
