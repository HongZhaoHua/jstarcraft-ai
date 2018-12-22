package com.jstarcraft.ai.neuralnetwork.loss;

import org.nd4j.linalg.api.ndarray.INDArray;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.CompositeMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;
import com.jstarcraft.ai.math.structure.matrix.Nd4jMatrix;

/**
 * L2目标函数
 * 
 * <pre></pre>
 * 
 * @author Birdy
 *
 */
public class L2LossFunction implements LossFunction {

	private float calculateScore(MathMatrix tests, MathMatrix trains) {
		float score = 0F;
		// TODO 此处需要重构.
		if (tests instanceof CompositeMatrix && trains instanceof CompositeMatrix) {
			MathMatrix[] testComponents = CompositeMatrix.class.cast(tests).getComponentMatrixes();
			MathMatrix[] trainComponents = CompositeMatrix.class.cast(trains).getComponentMatrixes();
			assert testComponents.length == trainComponents.length;
			for (int index = 0, size = testComponents.length; index < size; index++) {
				score += calculateScore(testComponents[index], trainComponents[index]);
			}
		} else if (tests instanceof Nd4jMatrix && trains instanceof Nd4jMatrix) {
			INDArray testArray = Nd4jMatrix.class.cast(tests).getArray();
			INDArray trainArray = Nd4jMatrix.class.cast(trains).getArray();
			INDArray scoreArray = trainArray.sub(testArray);
			scoreArray.muli(scoreArray);
			score = scoreArray.sumNumber().floatValue();
		} else {
			for (MatrixScalar term : trains) {
				double value = term.getValue();
				value = value - tests.getValue(term.getRow(), term.getColumn());
				score += value * value;
			}
		}
		// TODO 暂时不处理masks
		return score;
	}

	@Override
	public float computeScore(MathMatrix tests, MathMatrix trains, MathMatrix masks) {
		float score = calculateScore(tests, trains);
		return score;
	}

	private void calculateGradient(MathMatrix tests, MathMatrix trains, MathMatrix gradients) {
		// TODO 此处需要重构.
		if (tests instanceof CompositeMatrix && trains instanceof CompositeMatrix) {
			MathMatrix[] testComponents = CompositeMatrix.class.cast(tests).getComponentMatrixes();
			MathMatrix[] trainComponents = CompositeMatrix.class.cast(trains).getComponentMatrixes();
			MathMatrix[] gradientComponents = CompositeMatrix.class.cast(gradients).getComponentMatrixes();
			assert testComponents.length == trainComponents.length;
			for (int index = 0, size = testComponents.length; index < size; index++) {
				calculateGradient(testComponents[index], trainComponents[index], gradientComponents[index]);
			}
		} else if (tests instanceof Nd4jMatrix && trains instanceof Nd4jMatrix) {
			INDArray testArray = Nd4jMatrix.class.cast(tests).getArray();
			INDArray trainArray = Nd4jMatrix.class.cast(trains).getArray();
			INDArray gradientArray = Nd4jMatrix.class.cast(gradients).getArray();
			trainArray.sub(testArray, gradientArray);
			gradientArray.muli(2D);
		} else {
			gradients.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = trains.getValue(row, column);
				value = value - tests.getValue(row, column);
				scalar.setValue(value * 2F);
			});
		}
		// TODO 暂时不处理masks
	}

	@Override
	public void computeGradient(MathMatrix tests, MathMatrix trains, MathMatrix masks, MathMatrix gradients) {
		calculateGradient(tests, trains, gradients);
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
		return "L2LossFunction()";
	}

}
