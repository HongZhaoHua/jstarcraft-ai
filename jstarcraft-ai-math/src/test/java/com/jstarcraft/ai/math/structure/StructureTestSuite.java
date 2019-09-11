package com.jstarcraft.ai.math.structure;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.math.structure.matrix.MatrixTestSuite;
import com.jstarcraft.ai.math.structure.table.TableTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

        ScalarIteratorTestCase.class,

        MatrixTestSuite.class,

        TableTestSuite.class })
public class StructureTestSuite {

}
