package com.jstarcraft.ai.neuralnetwork.normalization;

import java.util.Map;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

public class IgnoreNormalizer implements Normalizer {

	@Override
	public void normalize(Map<String, MathMatrix> gradients) {
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "IgnoreNormalizer()";
	}

}
