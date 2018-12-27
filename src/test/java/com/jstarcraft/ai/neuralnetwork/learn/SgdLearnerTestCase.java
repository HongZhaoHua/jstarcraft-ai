package com.jstarcraft.ai.neuralnetwork.learn;

import org.nd4j.linalg.learning.GradientUpdater;
import org.nd4j.linalg.learning.SgdUpdater;
import org.nd4j.linalg.learning.config.Sgd;

import com.jstarcraft.ai.neuralnetwork.learn.Learner;
import com.jstarcraft.ai.neuralnetwork.learn.SgdLearner;

public class SgdLearnerTestCase extends LearnerTestCase {

	@Override
	protected GradientUpdater<?> getOldFunction(int[] shape) {
		Sgd configuration = new Sgd();
		GradientUpdater<?> oldFunction = new SgdUpdater(configuration);
		return oldFunction;
	}

	@Override
	protected Learner getNewFunction(int[] shape) {
		Learner newFuction = new SgdLearner();
		return newFuction;
	}

}
