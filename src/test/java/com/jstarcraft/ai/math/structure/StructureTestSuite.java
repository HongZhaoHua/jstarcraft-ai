package com.jstarcraft.ai.math.structure;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.math.structure.matrix.MatrixTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

		MathIteratorTestCase.class,

		MatrixTestSuite.class })
public class StructureTestSuite {

}
