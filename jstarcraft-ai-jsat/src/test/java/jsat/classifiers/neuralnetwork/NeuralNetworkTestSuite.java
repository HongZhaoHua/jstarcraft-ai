package jsat.classifiers.neuralnetwork;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

        BackPropagationNetTest.class,

        DReDNetSimpleTest.class,

        LVQLLCTest.class,

        LVQTest.class,

        PerceptronTest.class,

        RBFNetTest.class,

        SOMTest.class })
public class NeuralNetworkTestSuite {

}
