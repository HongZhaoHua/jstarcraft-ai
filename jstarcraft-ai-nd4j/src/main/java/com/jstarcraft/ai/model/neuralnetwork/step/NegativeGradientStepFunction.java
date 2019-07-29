package com.jstarcraft.ai.model.neuralnetwork.step;

import java.util.Map;
import java.util.Map.Entry;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

public class NegativeGradientStepFunction implements StepFunction {

	@Override
	public void step(float step, Map<String, MathMatrix> directions, Map<String, MathMatrix> parameters) {
		// TODO 考虑优化性能
		for (Entry<String, MathMatrix> keyValue : parameters.entrySet()) {
			MathMatrix parameter = keyValue.getValue();
			MathMatrix direction = directions.get(keyValue.getKey());
			parameter.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = scalar.getValue();
				scalar.setValue(value - direction.getValue(row, column));
			});
		}
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
		return "NegativeGradientStepFunction()";
	}

}
