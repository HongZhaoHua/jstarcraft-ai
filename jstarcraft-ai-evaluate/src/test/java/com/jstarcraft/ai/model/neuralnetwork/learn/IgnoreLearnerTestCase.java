package com.jstarcraft.ai.model.neuralnetwork.learn;

import org.nd4j.linalg.learning.GradientUpdater;
import org.nd4j.linalg.learning.NoOpUpdater;
import org.nd4j.linalg.learning.config.NoOp;

import com.jstarcraft.ai.model.neuralnetwork.learn.IgnoreLearner;
import com.jstarcraft.ai.model.neuralnetwork.learn.Learner;

public class IgnoreLearnerTestCase extends LearnerTestCase {

	@Override
	protected GradientUpdater<?> getOldFunction(long[] shape) {
		NoOp configuration = new NoOp();
		GradientUpdater<?> oldFunction = new NoOpUpdater(configuration);
		return oldFunction;
	}

	@Override
	protected Learner getNewFunction(long[] shape) {
		Learner newFuction = new IgnoreLearner();
		return newFuction;
	}

}
