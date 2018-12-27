package com.jstarcraft.ai.neuralnetwork;

public interface EpochMonitor {

	void beforeForward();

	void afterForward();

	void beforeBackward();

	void afterBackward();

}
