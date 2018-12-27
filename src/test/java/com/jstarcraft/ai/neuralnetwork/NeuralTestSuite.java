package com.jstarcraft.ai.neuralnetwork;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunctionTestSuite;
import com.jstarcraft.ai.neuralnetwork.layer.LayerTestSuite;
import com.jstarcraft.ai.neuralnetwork.learn.LearnerTestSuite;
import com.jstarcraft.ai.neuralnetwork.loss.LossFunctionTestSuite;
import com.jstarcraft.ai.neuralnetwork.vertex.VertexTestSuite;

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
