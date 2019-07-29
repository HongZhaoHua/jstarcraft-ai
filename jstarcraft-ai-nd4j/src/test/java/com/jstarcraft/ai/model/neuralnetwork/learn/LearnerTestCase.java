package com.jstarcraft.ai.model.neuralnetwork.learn;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.GradientUpdater;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.neuralnetwork.learn.Learner;
import com.jstarcraft.ai.modem.ModemCodec;
import com.jstarcraft.ai.utility.MathUtility;

public abstract class LearnerTestCase {

	private static DenseMatrix getMatrix(INDArray array) {
		DenseMatrix matrix = DenseMatrix.valueOf(array.rows(), array.columns());
		matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			scalar.setValue(array.getFloat(scalar.getRow(), scalar.getColumn()));
		});
		return matrix;
	}

	private static boolean equalMatrix(MathMatrix matrix, INDArray array) {
		for (int row = 0; row < matrix.getRowSize(); row++) {
			for (int column = 0; column < matrix.getColumnSize(); column++) {
				if (!MathUtility.equal(matrix.getValue(row, column), array.getFloat(row, column))) {
					return false;
				}
			}
		}
		return true;
	}

	protected abstract GradientUpdater<?> getOldFunction(long[] shape);

	protected abstract Learner getNewFunction(long[] shape);

	@Test
	public void testGradient() throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {
			long[] shape = { 5L, 2L };
			INDArray array = Nd4j.linspace(-2.5D, 2.0D, 10).reshape(shape);
			GradientUpdater<?> oldFunction = getOldFunction(shape);
			DenseMatrix gradient = getMatrix(array);
			Map<String, MathMatrix> gradients = new HashMap<>();
			gradients.put("gradients", gradient);
			Learner newFuction = getNewFunction(shape);
			newFuction.doCache(gradients);

			for (int iteration = 0; iteration < 10; iteration++) {
				oldFunction.applyUpdater(array, iteration, 0);
				newFuction.learn(gradients, iteration, 0);

				System.out.println(array);
				System.out.println(gradients);

				Assert.assertTrue(equalMatrix(gradient, array));
			}
		});
		task.get();
	}

	@Test
	public void testModel() {
		long[] shape = { 5L, 2L };
		Learner oldModel = getNewFunction(shape);
		for (ModemCodec codec : ModemCodec.values()) {
			byte[] data = codec.encodeModel(oldModel);
			Learner newModel = (Learner) codec.decodeModel(data);
			Assert.assertThat(newModel, CoreMatchers.equalTo(oldModel));
		}
	}

}
