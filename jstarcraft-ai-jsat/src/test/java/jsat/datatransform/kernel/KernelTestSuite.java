package jsat.datatransform.kernel;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        KernelPCATest.class,

        NystromTest.class,

        RFF_RBFTest.class })
public class KernelTestSuite {

}
