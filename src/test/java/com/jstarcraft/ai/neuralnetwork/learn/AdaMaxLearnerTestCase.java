package com.jstarcraft.ai.neuralnetwork.learn;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.AdaMaxUpdater;
import org.nd4j.linalg.learning.GradientUpdater;
import org.nd4j.linalg.learning.config.AdaMax;

import com.jstarcraft.ai.neuralnetwork.learn.AdaMaxLearner;
import com.jstarcraft.ai.neuralnetwork.learn.Learner;

public class AdaMaxLearnerTestCase extends LearnerTestCase {

	@Override
	protected GradientUpdater<?> getOldFunction(int[] shape) {
		AdaMax configuration = new AdaMax();
		GradientUpdater<?> oldFunction = new AdaMaxUpdater(configuration);
		int length = (int) (shape[0] * configuration.stateSize(shape[1]));
		INDArray view = Nd4j.zeros(length);
		oldFunction.setStateViewArray(view, shape, 'c', true);
		return oldFunction;
	}

	@Override
	protected Learner getNewFunction(int[] shape) {
		Learner newFuction = new AdaMaxLearner();
		return newFuction;
	}

}
