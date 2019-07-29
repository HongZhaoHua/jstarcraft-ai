package com.jstarcraft.ai.model.neuralnetwork.learn;

import org.nd4j.linalg.learning.GradientUpdater;
import org.nd4j.linalg.learning.SgdUpdater;
import org.nd4j.linalg.learning.config.Sgd;

import com.jstarcraft.ai.model.neuralnetwork.learn.Learner;
import com.jstarcraft.ai.model.neuralnetwork.learn.SgdLearner;

public class SgdLearnerTestCase extends LearnerTestCase {

	@Override
	protected GradientUpdater<?> getOldFunction(long	[] shape) {
		Sgd configuration = new Sgd();
		GradientUpdater<?> oldFunction = new SgdUpdater(configuration);
		return oldFunction;
	}

	@Override
	protected Learner getNewFunction(long[] shape) {
		Learner newFuction = new SgdLearner();
		return newFuction;
	}

}
