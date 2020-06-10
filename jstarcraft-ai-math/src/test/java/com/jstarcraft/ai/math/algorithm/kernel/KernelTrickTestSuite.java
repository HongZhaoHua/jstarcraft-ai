package com.jstarcraft.ai.math.algorithm.kernel;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AnovaKernelTrickTestCase.class,

        CauchyKernelTrickTestCase.class,

        ChiSquareKernelTrickTestCase.class,

        CircularKernelTrickTestCase.class,

        ExponentialKernelTrickTestCase.class,

        GaussianKernelTrickTestCase.class,

        HistogramIntersectionKernelTrickTestCase.class,

        LaplacianKernelTrickTestCase.class,

        LinearKernelTrickTestCase.class,

        LogKernelTrickTestCase.class,

        MultiquadricKernelTestCase.class,

        PolynomialKernelTrickTestCase.class,

        PowerKernelTrickTestCase.class,

        PukKernelTrickTestCase.class,

        QuadraticKernelTrickTestCase.class,

        SigmoidKernelTrickTestCase.class,

        SphericalKernelTrickTestCase.class,

        SplineKernelTrickTestCase.class,

        TstudentKernelTrickTestCase.class,

        WaveKernelTrickTestCase.class, })
public class KernelTrickTestSuite {

}
