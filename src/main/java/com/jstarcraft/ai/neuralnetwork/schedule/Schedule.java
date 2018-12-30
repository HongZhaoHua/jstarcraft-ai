package com.jstarcraft.ai.neuralnetwork.schedule;

public interface Schedule {

	float valueAt(int iteration, int epoch);

}
