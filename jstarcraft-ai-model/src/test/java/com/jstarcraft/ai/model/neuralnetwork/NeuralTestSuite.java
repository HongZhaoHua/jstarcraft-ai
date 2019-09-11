package com.jstarcraft.ai.model.neuralnetwork;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunctionTestSuite;
import com.jstarcraft.ai.model.neuralnetwork.layer.LayerTestSuite;
import com.jstarcraft.ai.model.neuralnetwork.learn.LearnerTestSuite;
import com.jstarcraft.ai.model.neuralnetwork.loss.LossFunctionTestSuite;
import com.jstarcraft.ai.model.neuralnetwork.vertex.VertexTestSuite;

@RunWith(Suite.class)
@SuiteClasses({

        GraphTestCase.class,

        ActivationFunctionTestSuite.class,

        LayerTestSuite.class,

        LearnerTestSuite.class,

        LossFunctionTestSuite.class,

        VertexTestSuite.class })
public class NeuralTestSuite {

}
