package com.jstarcraft.ai.math.algorithm.lsh;

import java.util.Arrays;
import java.util.Random;

import com.jstarcraft.ai.math.structure.vector.ArrayVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class ManhattanHashFunction implements VectorHashFunction {

	private int w;

	private MathVector projection;

	public ManhattanHashFunction(Random random, int dimensions, int width) {
		this.w = width;

		projection = new ArrayVector(dimensions, new float[dimensions]);
		for (int dimension = 0; dimension < dimensions; dimension++) {
			// mean 0
			// standard deviation 1.0
			float val = random.nextFloat() * w;
			projection.setValue(dimension, val);
		}
	}

	@Override
	public int hash(MathVector vector) {
		int hash[] = new int[projection.getDimensionSize()];
		for (int dimension = 0; dimension < projection.getDimensionSize(); dimension++) {
			hash[dimension] = (int) Math.floor((vector.getValue(dimension) - projection.getValue(dimension)) / Float.valueOf(w));
		}
		return Arrays.hashCode(hash);
	}

}
