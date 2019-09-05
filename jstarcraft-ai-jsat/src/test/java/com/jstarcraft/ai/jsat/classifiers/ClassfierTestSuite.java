package com.jstarcraft.ai.jsat.classifiers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.jsat.classifiers.bayesian.BayesianTestSuite;
import com.jstarcraft.ai.jsat.classifiers.boosting.EnsembleTestSuite;
import com.jstarcraft.ai.jsat.classifiers.calibration.CalibrationTestSuite;
import com.jstarcraft.ai.jsat.classifiers.evaluation.EvaluationTestSuite;
import com.jstarcraft.ai.jsat.classifiers.imbalance.ImbalanceTestSuite;
import com.jstarcraft.ai.jsat.classifiers.knn.KnnTestSuite;
import com.jstarcraft.ai.jsat.classifiers.linear.LinearTestSuite;
import com.jstarcraft.ai.jsat.classifiers.linear.kernelized.KernelizeTestSuite;
import com.jstarcraft.ai.jsat.classifiers.neuralnetwork.NeuralNetworkTestSuite;
import com.jstarcraft.ai.jsat.classifiers.svm.SvmTestSuite;
import com.jstarcraft.ai.jsat.classifiers.trees.TreeTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

        BayesianTestSuite.class,

        EnsembleTestSuite.class,

        CalibrationTestSuite.class,

        EvaluationTestSuite.class,

        ImbalanceTestSuite.class,

        KernelizeTestSuite.class,

        KnnTestSuite.class,

        LinearTestSuite.class,
        
        TreeTestSuite.class,
        
        NeuralNetworkTestSuite.class,
        
        SvmTestSuite.class,

        TreeTestSuite.class,

        DDAGTest.class,

        OneVSAllTest.class,

        OneVSOneTest.class,

        RocchioTest.class })
public class ClassfierTestSuite {

}
