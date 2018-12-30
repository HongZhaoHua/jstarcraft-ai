package com.jstarcraft.ai.neuralnetwork.layer;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.model.ModelCycle;
import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.core.utility.KeyValue;

/**
 * Embed层
 * 
 * <pre>
 * 只可以作为输入层
 * </pre>
 * 
 * @author Birdy
 *
 */
public class EmbedLayer extends WeightLayer implements ModelCycle {

	private KeyValue<MathVector, MathVector>[] weightReferences;

	protected EmbedLayer() {
		super();
	}

	public EmbedLayer(int numberOfInputs, int numberOfOutputs, MathCache factory, Map<String, ParameterConfigurator> configurators, Mode mode, ActivationFunction function) {
		super(numberOfInputs, numberOfOutputs, factory, configurators, mode, function);
		this.weightReferences = new KeyValue[numberOfInputs];
	}

	@Override
	public void doCache(MathCache factory, KeyValue<MathMatrix, MathMatrix> samples) {
		inputKeyValue = samples;
		int rowSize = inputKeyValue.getKey().getRowSize();
		int columnSize = inputKeyValue.getKey().getColumnSize();

		// 检查维度
		if (columnSize != 1) {
			throw new IllegalArgumentException();
		}

		middleKeyValue = new KeyValue<>(null, null);
		outputKeyValue = new KeyValue<>(null, null);

		MathMatrix middleData = factory.makeMatrix(rowSize, numberOfOutputs);
		middleKeyValue.setKey(middleData);
		MathMatrix middleError = factory.makeMatrix(rowSize, numberOfOutputs);
		middleKeyValue.setValue(middleError);

		MathMatrix outputData = factory.makeMatrix(rowSize, numberOfOutputs);
		outputKeyValue.setKey(outputData);
		MathMatrix innerError = factory.makeMatrix(rowSize, numberOfOutputs);
		outputKeyValue.setValue(innerError);
	}

	@Override
	public void doForward() {
		MathMatrix weightParameters = parameters.get(WEIGHT_KEY);
		MathMatrix biasParameters = parameters.get(BIAS_KEY);
		MathMatrix weightGradients = gradients.get(WEIGHT_KEY);

		MathMatrix inputData = getMatrix(inputKeyValue.getKey());
		MathMatrix middleData = getMatrix(middleKeyValue.getKey());
		MathMatrix outputData = getMatrix(outputKeyValue.getKey());

		// inputData.dotProduct(weightParameters, middleData);
		// TODO 考虑并发操作
		middleData.setValues(0F);
		int rowSize = middleData.getRowSize();
		EnvironmentContext context = EnvironmentContext.getContext();
		CountDownLatch latch = new CountDownLatch(rowSize);
		for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
			int cursor = rowIndex;
			context.doStructureByAny(cursor, () -> {
				try {
					int index = (int) inputData.getValue(cursor, 0);
					// 索引为负数代表不输出
					if (index >= 0) {
						KeyValue<MathVector, MathVector> keyValue = weightReferences[index];
						if (keyValue == null) {
							keyValue = new KeyValue(weightParameters.getRowVector(index), weightGradients.getRowVector(index));
							weightReferences[index] = keyValue;
						}
						middleData.getRowVector(cursor).copyVector(keyValue.getKey());
						// for (int columnIndex = 0, columnSize =
						// middleData.getColumnSize(); columnIndex < columnSize;
						// columnIndex++) {
						// double value = weightParameters.getValue(index,
						// columnIndex);
						// middleData.setValue(rowIndex, columnIndex, value);
						// }
					}
				} finally {
					latch.countDown();
				}
			});
		}
		try {
			latch.await();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
		if (biasParameters != null) {
			middleData.addRowVector(biasParameters.getRowVector(0));
			// for (int columnIndex = 0, columnSize =
			// middleData.getColumnSize(); columnIndex < columnSize;
			// columnIndex++) {
			// double bias = biasParameters.getValue(0, columnIndex);
			// middleData.getColumnVector(columnIndex).shiftValues(bias);
			// }
		}

		function.forward(middleData, outputData);

		MathMatrix middleError = middleKeyValue.getValue();
		middleError.setValues(0F);

		MathMatrix innerError = outputKeyValue.getValue();
		innerError.setValues(0F);
	}

	@Override
	public void doBackward() {
		MathMatrix weightParameters = parameters.get(WEIGHT_KEY);
		MathMatrix biasParameters = parameters.get(BIAS_KEY);
		MathMatrix weightGradients = gradients.get(WEIGHT_KEY);
		MathMatrix biasGradients = gradients.get(BIAS_KEY);

		MathMatrix innerError = getMatrix(outputKeyValue.getValue());
		MathMatrix middleError = getMatrix(middleKeyValue.getValue());
		// 必须为null
		MathMatrix outerError = getMatrix(inputKeyValue.getValue());
		MathMatrix inputData = getMatrix(inputKeyValue.getKey());
		MathMatrix middleData = getMatrix(middleKeyValue.getKey());
		MathMatrix outputData = getMatrix(outputKeyValue.getKey());

		// 计算梯度
		function.backward(middleData, innerError, middleError);

		// inputData.transposeProductThat(middleError, weightGradients);
		weightGradients.setValues(0F);
		int rowSize = middleData.getRowSize();
		for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
			// TODO 此处可以想办法支持并发,得注意根据index同步.
			int index = (int) inputData.getValue(rowIndex, 0);
			if (index >= 0) {
				KeyValue<MathVector, MathVector> keyValue = weightReferences[index];
				keyValue.getValue().addVector(middleError.getRowVector(rowIndex));
			}
		}
		if (biasGradients != null) {
			for (int columnIndex = 0, columnSize = biasGradients.getColumnSize(); columnIndex < columnSize; columnIndex++) {
				float bias = middleError.getColumnVector(columnIndex).getSum(false);
				biasGradients.setValue(0, columnIndex, bias);
			}
		}
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void afterLoad() {
		weightReferences = new KeyValue[numberOfInputs];
	}

}
