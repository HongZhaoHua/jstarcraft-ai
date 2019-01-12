package com.jstarcraft.ai.math.structure.matrix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

		ColumnArrayMatrixTestCase.class,

		ColumnCompositeMatrixTestCase.class,

		ColumnRandomMatrixTestCase.class,

		DenseMatrixTestCase.class,

		Nd4jMatrixTestCase.class,

		RowArrayMatrixTestCase.class,

		RowCompositeMatrixTestCase.class,

		RowRandomMatrixTestCase.class,

		SparseMatrixTestCase.class,

		SymmetryMatrixTestCase.class })
public class MatrixTestSuite {

}
