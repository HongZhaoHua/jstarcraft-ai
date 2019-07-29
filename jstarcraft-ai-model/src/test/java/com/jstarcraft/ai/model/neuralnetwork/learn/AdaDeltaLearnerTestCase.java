package com.jstarcraft.ai.model.neuralnetwork.learn;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.AdaDeltaUpdater;
import org.nd4j.linalg.learning.GradientUpdater;
import org.nd4j.linalg.learning.config.AdaDelta;

import com.jstarcraft.ai.model.neuralnetwork.learn.AdaDeltaLearner;
import com.jstarcraft.ai.model.neuralnetwork.learn.Learner;

public class AdaDeltaLearnerTestCase extends LearnerTestCase {

	@Override
	protected GradientUpdater<?> getOldFunction(long[] shape) {
		AdaDelta configuration = new AdaDelta();
		GradientUpdater<?> oldFunction = new AdaDeltaUpdater(configuration);
		int length = (int) (shape[0] * configuration.stateSize(shape[1]));
		INDArray view = Nd4j.zeros(length);
		oldFunction.setStateViewArray(view, shape, 'c', true);
		return oldFunction;
	}

	@Override
	protected Learner getNewFunction(long[] shape) {
		Learner newFuction = new AdaDeltaLearner();
		return newFuction;
	}

}
