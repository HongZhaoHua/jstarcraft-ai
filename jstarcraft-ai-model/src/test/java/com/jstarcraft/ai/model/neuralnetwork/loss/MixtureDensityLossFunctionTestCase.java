package com.jstarcraft.ai.model.neuralnetwork.loss;

import java.util.LinkedList;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.activations.IActivation;
import org.nd4j.linalg.activations.impl.ActivationSigmoid;
import org.nd4j.linalg.activations.impl.ActivationSoftmax;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.ILossFunction;
import org.nd4j.linalg.lossfunctions.impl.LossMixtureDensity;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentFactory;
import com.jstarcraft.ai.math.MathUtility;
import com.jstarcraft.ai.math.structure.matrix.ColumnGlobalMatrix;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.activation.SigmoidActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.activation.SoftMaxActivationFunction;
import com.jstarcraft.core.utility.KeyValue;

public class MixtureDensityLossFunctionTestCase extends LossFunctionTestCase {

	protected static ColumnGlobalMatrix getMatrix(int rowSize, int columnSize) {
		MathMatrix[] matrixes = new MathMatrix[columnSize];
		for (int index = 0; index < columnSize; index++) {
			matrixes[index] = DenseMatrix.valueOf(rowSize, 1);
		}
		return ColumnGlobalMatrix.attachOf(matrixes);
	}

	@Override
	protected ILossFunction getOldFunction() {
		return LossMixtureDensity.builder().gaussians(1).labelWidth(2).build();
	}

	@Override
	protected LossFunction getNewFunction(ActivationFunction function) {
		return new MixtureDensityLossFunction(1, 2);
	}

	@Test
	@Override
	public void testScore() throws Exception {
		EnvironmentContext context = EnvironmentFactory.getContext();
		Future<?> task = context.doTask(() -> {
			LinkedList<KeyValue<IActivation, ActivationFunction>> activetionList = new LinkedList<>();
			activetionList.add(new KeyValue<>(new ActivationSigmoid(), new SigmoidActivationFunction()));
			activetionList.add(new KeyValue<>(new ActivationSoftmax(), new SoftMaxActivationFunction()));
			for (KeyValue<IActivation, ActivationFunction> keyValue : activetionList) {
				INDArray array = Nd4j.linspace(-2.5D, 2.0D, 20).reshape(5, 4);
				INDArray marks = Nd4j.create(new double[] { 0D, 1D, 0D, 1D, 0D, 1D, 0D, 1D, 0D, 1D }).reshape(5, 2);
				ILossFunction oldFunction = getOldFunction();
				float value = (float) oldFunction.computeScore(marks, array.dup(), keyValue.getKey(), null, false);

				MathMatrix input = getMatrix(array.rows(), array.columns()).copyMatrix(getMatrix(array), false);
				MathMatrix output = getMatrix(input.getRowSize(), input.getColumnSize());
				ActivationFunction function = keyValue.getValue();
				function.forward(input, output);
				LossFunction newFunction = getNewFunction(function);
				newFunction.doCache(getMatrix(marks.rows(), marks.columns()).copyMatrix(getMatrix(marks), false), output);
				float score = newFunction.computeScore(getMatrix(marks.rows(), marks.columns()).copyMatrix(getMatrix(marks), false), output, null);

				System.out.println(value);
				System.out.println(score);
				if (!MathUtility.equal(value, score)) {
					Assert.fail();
				}
			}
		});
		task.get();
	}

	@Test
	@Override
	public void testGradient() throws Exception {
		EnvironmentContext context = EnvironmentFactory.getContext();
		Future<?> task = context.doTask(() -> {
			LinkedList<KeyValue<IActivation, ActivationFunction>> activetionList = new LinkedList<>();
			activetionList.add(new KeyValue<>(new ActivationSigmoid(), new SigmoidActivationFunction()));
			activetionList.add(new KeyValue<>(new ActivationSoftmax(), new SoftMaxActivationFunction()));
			for (KeyValue<IActivation, ActivationFunction> keyValue : activetionList) {
				INDArray array = Nd4j.linspace(-2.5D, 2.0D, 20).reshape(5, 4);
				INDArray marks = Nd4j.create(new double[] { 0D, 1D, 0D, 1D, 0D, 1D, 0D, 1D, 0D, 1D }).reshape(5, 2);
				ILossFunction oldFunction = getOldFunction();
				INDArray value = oldFunction.computeGradient(marks, array.dup(), keyValue.getKey(), null);

				MathMatrix input = getMatrix(array.rows(), array.columns()).copyMatrix(getMatrix(array), false);
				MathMatrix output = getMatrix(input.getRowSize(), input.getColumnSize());
				ActivationFunction function = keyValue.getValue();
				function.forward(input, output);
				MathMatrix gradient = getMatrix(input.getRowSize(), input.getColumnSize());
				LossFunction newFunction = getNewFunction(function);
				newFunction.doCache(getMatrix(marks.rows(), marks.columns()).copyMatrix(getMatrix(marks), false), output);
				newFunction.computeGradient(getMatrix(marks.rows(), marks.columns()).copyMatrix(getMatrix(marks), false), output, null, gradient);
				function.backward(input, gradient, output);
				System.out.println(value);
				System.out.println(output);
				Assert.assertTrue(equalMatrix(output, value));
			}
		});
		task.get();
	}

}
