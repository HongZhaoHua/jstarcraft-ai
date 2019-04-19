package com.jstarcraft.ai.neuralnetwork.loss;

import java.util.LinkedList;
import java.util.concurrent.Future;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.activations.IActivation;
import org.nd4j.linalg.activations.impl.ActivationSigmoid;
import org.nd4j.linalg.activations.impl.ActivationSoftmax;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.ILossFunction;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.modem.ModemCodec;
import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.activation.SigmoidActivationFunction;
import com.jstarcraft.ai.neuralnetwork.activation.SoftMaxActivationFunction;
import com.jstarcraft.ai.utility.MathUtility;
import com.jstarcraft.core.utility.KeyValue;

public abstract class LossFunctionTestCase {

	protected static DenseMatrix getMatrix(INDArray array) {
		DenseMatrix matrix = DenseMatrix.valueOf(array.rows(), array.columns());
		matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			scalar.setValue(array.getFloat(scalar.getRow(), scalar.getColumn()));
		});
		return matrix;
	}

	protected static boolean equalMatrix(MathMatrix matrix, INDArray array) {
		for (int row = 0; row < matrix.getRowSize(); row++) {
			for (int column = 0; column < matrix.getColumnSize(); column++) {
				if (Math.abs(matrix.getValue(row, column) - array.getFloat(row, column)) > MathUtility.EPSILON) {
					return false;
				}
			}
		}
		return true;
	}

	protected abstract ILossFunction getOldFunction();

	protected abstract LossFunction getNewFunction(ActivationFunction function);

	@Test
	public void testScore() throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {
			LinkedList<KeyValue<IActivation, ActivationFunction>> activetionList = new LinkedList<>();
			activetionList.add(new KeyValue<>(new ActivationSigmoid(), new SigmoidActivationFunction()));
			activetionList.add(new KeyValue<>(new ActivationSoftmax(), new SoftMaxActivationFunction()));
			for (KeyValue<IActivation, ActivationFunction> keyValue : activetionList) {
				INDArray array = Nd4j.linspace(-2.5D, 2.0D, 10).reshape(5, 2);
				INDArray marks = Nd4j.create(new double[] { 0D, 1D, 0D, 1D, 0D, 1D, 0D, 1D, 0D, 1D }).reshape(5, 2);
				ILossFunction oldFunction = getOldFunction();
				double value = oldFunction.computeScore(marks, array.dup(), keyValue.getKey(), null, false);

				DenseMatrix input = getMatrix(array);
				DenseMatrix output = DenseMatrix.valueOf(input.getRowSize(), input.getColumnSize());
				ActivationFunction function = keyValue.getValue();
				function.forward(input, output);
				LossFunction newFunction = getNewFunction(function);
				newFunction.doCache(getMatrix(marks), output);
				double score = newFunction.computeScore(getMatrix(marks), output, null);

				System.out.println(value);
				System.out.println(score);

				if (Math.abs(value - score) > MathUtility.EPSILON) {
					Assert.fail();
				}
			}
		});
		task.get();
	}

	@Test
	public void testGradient() throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {
			LinkedList<KeyValue<IActivation, ActivationFunction>> activetionList = new LinkedList<>();
			activetionList.add(new KeyValue<>(new ActivationSigmoid(), new SigmoidActivationFunction()));
			activetionList.add(new KeyValue<>(new ActivationSoftmax(), new SoftMaxActivationFunction()));
			for (KeyValue<IActivation, ActivationFunction> keyValue : activetionList) {
				INDArray array = Nd4j.linspace(-2.5D, 2.0D, 10).reshape(5, 2);
				INDArray marks = Nd4j.create(new double[] { 0D, 1D, 0D, 1D, 0D, 1D, 0D, 1D, 0D, 1D }).reshape(5, 2);
				ILossFunction oldFunction = getOldFunction();
				INDArray value = oldFunction.computeGradient(marks, array.dup(), keyValue.getKey(), null);

				DenseMatrix input = getMatrix(array);
				DenseMatrix output = DenseMatrix.valueOf(input.getRowSize(), input.getColumnSize());
				ActivationFunction function = keyValue.getValue();
				function.forward(input, output);
				DenseMatrix gradient = DenseMatrix.valueOf(input.getRowSize(), input.getColumnSize());
				LossFunction newFunction = getNewFunction(function);
				newFunction.doCache(getMatrix(marks), output);
				newFunction.computeGradient(getMatrix(marks), output, null, gradient);
				function.backward(input, gradient, output);
				System.out.println(value);
				System.out.println(output);
				Assert.assertTrue(equalMatrix(output, value));
			}
		});
		task.get();
	}

	@Test
	public void testModel() {
		LossFunction oldModel = getNewFunction(null);
		for (ModemCodec codec : ModemCodec.values()) {
			byte[] data = codec.encodeModel(oldModel);
			LossFunction newModel = (LossFunction) codec.decodeModel(data);
			Assert.assertThat(newModel, CoreMatchers.equalTo(oldModel));
		}
	}

}
