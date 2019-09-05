package com.jstarcraft.ai.jsat.datatransform;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.jsat.datatransform.featureselection.FeatureSelectionTestSuite;
import com.jstarcraft.ai.jsat.datatransform.kernel.KernelTestSuite;
import com.jstarcraft.ai.jsat.datatransform.visualization.VisualizationTestSuite;

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
