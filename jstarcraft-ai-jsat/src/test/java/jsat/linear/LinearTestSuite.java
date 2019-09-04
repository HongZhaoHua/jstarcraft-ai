package jsat.linear;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jsat.linear.distancemetrics.DistanceTestSuite;
import jsat.linear.solvers.SolverTestSuite;
import jsat.linear.vectorcollection.VectorCollectionTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

        DistanceTestSuite.class,

        SolverTestSuite.class,

        VectorCollectionTestSuite.class,

        CholeskyDecompositionTest.class,

        ConcatenatedVecTest.class,

        DenseMatrixTest.class,

        EigenvalueDecompositionTest.class,

        GenericMatrixTest.class,

        HessenbergFormTest.class,

        LUPDecompositionTest.class,

        MatrixOfVecsTest.class,

        MatrixStatisticsTest.class,

        MatrixTest.class,

        Poly2VecTest.class,

        QRDecompositionTest.class,

        ShiftedVecTest.class,

        SingularValueDecompositionTest.class,

        SparseVectorTest.class,

        SubVectorTest.class,

        VecWithNormTest.class })
public class LinearTestSuite {

}
