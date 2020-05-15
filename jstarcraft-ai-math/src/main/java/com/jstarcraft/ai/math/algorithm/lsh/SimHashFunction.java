package com.jstarcraft.ai.math.algorithm.lsh;

import java.util.Random;

import com.jstarcraft.ai.math.structure.DefaultScalar;
import com.jstarcraft.ai.math.structure.MathScalar;
import com.jstarcraft.ai.math.structure.vector.ArrayVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class SimHashFunction implements VectorHashFunction {

	private MathVector projection;

	public SimHashFunction(Random random, MathVector projection) {
		int dimensions = projection.getElementSize();
		this.projection = new ArrayVector(dimensions, new float[dimensions]);
		for (int dimension = 0; dimension < dimensions; dimension++) {
			float value = projection.getValue(dimension);
			this.projection.setValue(dimension, random.nextBoolean() ? value : -value);
		}
	}

	@Override
	public int hash(MathVector vector) {
		MathScalar scalar = DefaultScalar.getInstance();
		// calculate the dot product.
		float hash = scalar.dotProduct(vector, projection).getValue();
		// returns a 'bit' encoded as an integer.
		// 1 when positive or zero, 0 otherwise.
		return hash > 0 ? 1 : 0;
	}
}
