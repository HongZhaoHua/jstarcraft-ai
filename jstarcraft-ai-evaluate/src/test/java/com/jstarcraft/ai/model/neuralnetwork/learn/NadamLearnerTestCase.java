package com.jstarcraft.ai.model.neuralnetwork.learn;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.GradientUpdater;
import org.nd4j.linalg.learning.NadamUpdater;
import org.nd4j.linalg.learning.config.Nadam;

import com.jstarcraft.ai.model.neuralnetwork.learn.Learner;
import com.jstarcraft.ai.model.neuralnetwork.learn.NadamLearner;

public class NadamLearnerTestCase extends LearnerTestCase {

	@Override
	protected GradientUpdater<?> getOldFunction(long[] shape) {
		Nadam configuration = new Nadam();
		GradientUpdater<?> oldFunction = new NadamUpdater(configuration);
		int length = (int) (shape[0] * configuration.stateSize(shape[1]));
		INDArray view = Nd4j.zeros(length);
		oldFunction.setStateViewArray(view, shape, 'c', true);
		return oldFunction;
	}

	@Override
	protected Learner getNewFunction(long[] shape) {
		Learner newFuction = new NadamLearner();
		return newFuction;
	}

}
