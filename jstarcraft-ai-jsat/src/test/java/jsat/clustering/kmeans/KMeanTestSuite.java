package jsat.clustering.kmeans;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        ElkanKernelKMeansTest.class,

        ElkanKMeansTest.class,

        GMeansTest.class,

        HamerlyKMeansTest.class,

        KMeansPDNTest.class,

        LloydKernelKMeansTest.class,

        MiniBatchKMeansTest.class,

        NaiveKMeansTest.class,

        XMeansTest.class })
public class KMeanTestSuite {

}
