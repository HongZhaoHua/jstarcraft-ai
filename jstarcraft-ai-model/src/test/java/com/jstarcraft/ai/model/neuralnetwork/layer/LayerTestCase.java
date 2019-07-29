package com.jstarcraft.ai.model.neuralnetwork.layer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.deeplearning4j.nn.gradient.Gradient;
import org.deeplearning4j.nn.layers.AbstractLayer;
import org.deeplearning4j.nn.workspace.LayerWorkspaceMgr;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.primitives.Pair;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentFactory;
import com.jstarcraft.ai.math.structure.DenseCache;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.neuralnetwork.layer.Layer;
import com.jstarcraft.ai.model.neuralnetwork.vertex.LayerVertex;
import com.jstarcraft.ai.modem.ModemCodec;
import com.jstarcraft.ai.utility.MathUtility;
import com.jstarcraft.core.utility.KeyValue;

public abstract class LayerTestCase {

	protected static DenseMatrix getMatrix(INDArray array) {
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

	protected abstract INDArray getData();

	protected abstract INDArray getError();

	protected abstract List<KeyValue<String, String>> getGradients();

	protected abstract AbstractLayer<?> getOldFunction();

	protected abstract Layer getNewFunction(AbstractLayer<?> layer);

	@Test
	public void testPropagate() throws Exception {
		EnvironmentContext context = EnvironmentFactory.getContext();
		LayerWorkspaceMgr space = LayerWorkspaceMgr.noWorkspacesImmutable();
		Future<?> task = context.doTask(() -> {
			INDArray array = getData();
			MathCache cache = new DenseCache();
			AbstractLayer<?> oldFunction = getOldFunction();
			Layer newFuction = getNewFunction(oldFunction);
			LayerVertex newVertex = new LayerVertex("new", cache, newFuction);
			{
				DenseMatrix key = getMatrix(array);
				DenseMatrix value = DenseMatrix.valueOf(key.getRowSize(), key.getColumnSize());
				KeyValue<MathMatrix, MathMatrix> keyValue = new KeyValue<>(key, value);
				newVertex.doCache(keyValue);
			}

			// 正向传播
			oldFunction.setInput(array, space);
			INDArray value = oldFunction.activate(true, space);
			newVertex.doForward();
			KeyValue<MathMatrix, MathMatrix> output = newVertex.getOutputKeyValue();
			System.out.println(value);
			System.out.println(output.getKey());
			Assert.assertTrue(equalMatrix(output.getKey(), value));

			// 反向传播
			INDArray previousEpsilon = getError();
			Pair<Gradient, INDArray> keyValue = oldFunction.backpropGradient(previousEpsilon, space);
			INDArray nextEpsilon = keyValue.getValue();
			output.getValue().copyMatrix(getMatrix(previousEpsilon), false);
			newVertex.doBackward();
			MathMatrix error = newVertex.getInputKeyValue(0).getValue();
			System.out.println(nextEpsilon);
			System.out.println(error);
			if (nextEpsilon != null) {
				Assert.assertTrue(equalMatrix(error, nextEpsilon));
			}

			// 梯度
			Map<String, INDArray> oldGradients = keyValue.getKey().gradientForVariable();
			Map<String, MathMatrix> newGradients = newFuction.getGradients();
			for (KeyValue<String, String> gradient : getGradients()) {
				INDArray oldGradient = oldGradients.get(gradient.getKey());
				MathMatrix newGradient = newGradients.get(gradient.getValue());
				System.out.println(oldGradient);
				System.out.println(newGradient);
				Assert.assertTrue(equalMatrix(newGradient, oldGradient));
			}
		});
		task.get();
	}

	@Test
	public void testModel() {
		AbstractLayer<?> oldFunction = getOldFunction();
		Layer oldModel = getNewFunction(oldFunction);
		for (ModemCodec codec : ModemCodec.values()) {
			byte[] data = codec.encodeModel(oldModel);
			Layer newModel = (Layer) codec.decodeModel(data);
			Assert.assertThat(newModel, CoreMatchers.equalTo(oldModel));
		}
	}

}
