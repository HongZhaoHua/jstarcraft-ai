package jsat.classifiers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jsat.classifiers.bayesian.BayesianTestSuite;
import jsat.classifiers.boosting.EnsembleTestSuite;
import jsat.classifiers.calibration.CalibrationTestSuite;
import jsat.classifiers.evaluation.EvaluationTestSuite;
import jsat.classifiers.imbalance.ImbalanceTestSuite;
import jsat.classifiers.knn.KnnTestSuite;
import jsat.classifiers.linear.LinearTestSuite;
import jsat.classifiers.linear.kernelized.KernelizeTestSuite;
import jsat.classifiers.neuralnetwork.NeuralNetworkTestSuite;
import jsat.classifiers.svm.SvmTestSuite;
import jsat.classifiers.trees.TreeTestSuite;

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
