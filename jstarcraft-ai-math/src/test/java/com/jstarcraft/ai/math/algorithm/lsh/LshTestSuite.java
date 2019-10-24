package com.jstarcraft.ai.math.algorithm.lsh;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        CosineHashFamilyTestCase.class,

        EuclideanHashFamilyTestCase.class,

        ManhattanHashFamilyTestCase.class,

        MinHashFamilyTestCase.class, })
public class LshTestSuite {

}
