package com.jstarcraft.ai.math.algorithm.lsh;

import java.util.Random;

import com.jstarcraft.ai.math.algorithm.correlation.distance.HammingDistance;
import com.jstarcraft.ai.math.structure.vector.ArrayVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class SimHashFamily implements LshHashFamily {

	private static final HammingDistance distance = new HammingDistance();

	private final int dimensions;

	private int w;

	private MathVector projection;

	public SimHashFamily(Random random, int dimensions, int w) {
		this.dimensions = dimensions;
		this.w = w;
		this.projection = new ArrayVector(dimensions, new float[dimensions]);
		for (int dimension = 0; dimension < dimensions; dimension++) {
			float value = random.nextInt(w) + 1;
			projection.setValue(dimension, value);
		}
	}

	@Override
	public VectorHashFunction getHashFunction(Random random) {
		return new SimHashFunction(random, projection);
	}

	@Override
	public float getCoefficient(MathVector leftVector, MathVector rightVector) {
		return distance.getCoefficient(leftVector, rightVector);
	}

}
