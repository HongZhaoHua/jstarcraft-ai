package jsat.linear.distancemetrics;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        ChebyshevDistanceTest.class,

        CosineDistanceTest.class,

        CosineDistanceTestNormalized.class,

        EuclideanDistanceTest.class,

        JaccardDistanceTest.class,

        MahalanobisDistanceTest.class,

        ManhattanDistanceTest.class,

        MinkowskiDistanceTest.class,

        NormalizedEuclideanDistanceTest.class,

        PearsonDistanceTest.class,

        SquaredEuclideanDistanceTest.class,

        WeightedEuclideanDistanceTest.class })
public class DistanceTestSuite {

}
