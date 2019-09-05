package jsat.datatransform;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jsat.datatransform.featureselection.FeatureSelectionTestSuite;
import jsat.datatransform.kernel.KernelTestSuite;
import jsat.datatransform.visualization.VisualizationTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

        FeatureSelectionTestSuite.class,

        KernelTestSuite.class,

        VisualizationTestSuite.class,

        FastICATest.class,

        ImputerTest.class,

        JLTransformTest.class,

        PCATest.class,

        RemoveAttributeTransformTest.class,

        WhitenedPCATest.class,

        WhitenedZCATest.class })
public class DataTransformTestSuite {

}
