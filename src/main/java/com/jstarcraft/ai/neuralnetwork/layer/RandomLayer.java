package com.jstarcraft.ai.neuralnetwork.layer;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.ColumnCompositeMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.vector.CompositeVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.core.utility.KeyValue;

/**
 * 不定层
 * 
 * <pre>
 * 本质核心其实是一种参数共享
 * </pre>
 * 
 * @author Birdy
 *
 */
public class RandomLayer extends WeightLayer {

	private int numberOfRandoms;

	private MathCache factory;

	public RandomLayer(int numberOfInputs, int numberOfOutputs, int numberOfRandoms, MathCache factory, Map<String, ParameterConfigurator> configurators, Mode mode, ActivationFunction function) {
		super(numberOfInputs, numberOfOutputs, factory, configurators, mode, function);
		this.numberOfRandoms = numberOfRandoms;
	}

	@Override
	public void doCache(MathCache factory, KeyValue<MathMatrix, MathMatrix> samples) {
		this.factory = factory;
		inputKeyValue = samples;
		int rowSize = inputKeyValue.getKey().getRowSize();
		int columnSize = inputKeyValue.getKey().getColumnSize();

		// 检查维度
		if (columnSize != 1 + numberOfInputs * numberOfRandoms) {
			throw new IllegalArgumentException();
		}

		columnSize = numberOfOutputs * numberOfRandoms;
		middleKeyValue = new KeyValue<>(null, null);
		outputKeyValue = new KeyValue<>(null, null);

		int size = numberOfRandoms;
		MathMatrix[] middleDatas = new MathMatrix[size];
		MathMatrix[] middleErrors = new MathMatrix[size];
		MathMatrix[] outputDatas = new MathMatrix[size + 1];
		MathMatrix[] innerErrors = new MathMatrix[size + 1];
		outputDatas[0] = factory.makeMatrix(rowSize, 1);
		innerErrors[0] = factory.makeMatrix(rowSize, 1);
		for (int index = 0; index < size; index++) {
			middleDatas[index] = factory.makeMatrix(rowSize, numberOfOutputs);
			middleErrors[index] = factory.makeMatrix(rowSize, numberOfOutputs);
			outputDatas[index + 1] = factory.makeMatrix(rowSize, numberOfOutputs);
			innerErrors[index + 1] = factory.makeMatrix(rowSize, numberOfOutputs);
		}

		MathMatrix middleData = ColumnCompositeMatrix.attachOf(middleDatas);
		middleKeyValue.setKey(middleData);
		MathMatrix middleError = ColumnCompositeMatrix.attachOf(middleErrors);
		middleKeyValue.setValue(middleError);

		MathMatrix outputData = ColumnCompositeMatrix.attachOf(outputDatas);
		outputKeyValue.setKey(outputData);
		MathMatrix innerError = ColumnCompositeMatrix.attachOf(innerErrors);
		outputKeyValue.setValue(innerError);
	}

	@Override
	public void doForward() {
		MathMatrix weightParameters = parameters.get(WEIGHT_KEY);
		MathMatrix biasParameters = parameters.get(BIAS_KEY);

		MathMatrix inputData = inputKeyValue.getKey();
		MathMatrix middleData = middleKeyValue.getKey();
		middleData.setValues(0F);
		MathMatrix outputData = outputKeyValue.getKey();
		outputData.setValues(0F);
		outputData.getColumnVector(0).copyVector(inputData.getColumnVector(0));
		int numberOfRows = inputData.getRowSize();
		EnvironmentContext context = EnvironmentContext.getContext();
		CountDownLatch latch = new CountDownLatch(numberOfRows);
		for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
			MathVector inputMajorData = inputData.getRowVector(rowIndex);
			MathVector middleMajorData = middleData.getRowVector(rowIndex);
			MathVector outputMajorData = outputData.getRowVector(rowIndex);
			int numberOfColumns = (int) inputMajorData.getValue(0);
			context.doStructureByAny(rowIndex, () -> {
				for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
					MathVector inputMinorData = CompositeVector.detachOf(CompositeVector.class.cast(inputMajorData), columnIndex * numberOfInputs + 1, (columnIndex + 1) * numberOfInputs + 1);
					MathVector middleMinorData = CompositeVector.detachOf(CompositeVector.class.cast(middleMajorData), columnIndex * numberOfOutputs, (columnIndex + 1) * numberOfOutputs);
					MathVector outputMinorData = CompositeVector.detachOf(CompositeVector.class.cast(outputMajorData), columnIndex * numberOfOutputs + 1, (columnIndex + 1) * numberOfOutputs + 1);
					middleMinorData.dotProduct(inputMinorData, weightParameters, false, MathCalculator.SERIAL);
					if (biasParameters != null) {
						middleMinorData.iterateElement(MathCalculator.SERIAL, (scalar) -> {
							int index = scalar.getIndex();
							float value = scalar.getValue();
							scalar.setValue(value + biasParameters.getValue(0, index));
						});
					}
					function.forward(middleMajorData, outputMinorData);
				}
				latch.countDown();
			});
		}
		try {
			latch.await();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}

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
		weightGradients.setValues(0F);
		MathMatrix biasGradients = gradients.get(BIAS_KEY);
		biasGradients.setValues(0F);

		MathMatrix inputData = inputKeyValue.getKey();
		MathMatrix middleData = middleKeyValue.getKey();
		MathMatrix outputData = outputKeyValue.getKey();

		MathMatrix innerError = outputKeyValue.getValue();
		MathMatrix middleError = middleKeyValue.getValue();
		MathMatrix outerError = inputKeyValue.getValue();

		int numberOfInstances = 0;
		int numberOfRows = inputData.getRowSize();
		for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
			MathVector inputMajorData = inputData.getRowVector(rowIndex);
			MathVector middleMajorData = middleData.getRowVector(rowIndex);
			MathVector outputMajorData = outputData.getRowVector(rowIndex);
			MathVector innerMajorError = innerError.getRowVector(rowIndex);
			MathVector middleMajorError = middleError.getRowVector(rowIndex);
			MathVector outerMajorError = outerError == null ? null : outerError.getRowVector(rowIndex);
			int numberOfColumns = (int) inputMajorData.getValue(0);
			numberOfInstances += numberOfColumns;
			for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
				MathVector inputMinorData = CompositeVector.detachOf(CompositeVector.class.cast(inputMajorData), columnIndex * numberOfInputs + 1, (columnIndex + 1) * numberOfInputs + 1);
				MathVector middleMinorData = CompositeVector.detachOf(CompositeVector.class.cast(middleMajorData), columnIndex * numberOfOutputs, (columnIndex + 1) * numberOfOutputs);
				MathVector innerMinorError = CompositeVector.detachOf(CompositeVector.class.cast(innerMajorError), columnIndex * numberOfOutputs + 1, (columnIndex + 1) * numberOfOutputs + 1);
				MathVector middleMinorError = CompositeVector.detachOf(CompositeVector.class.cast(middleMajorError), columnIndex * numberOfOutputs, (columnIndex + 1) * numberOfOutputs);

				// 计算梯度
				function.backward(middleMinorData, innerMinorError, middleMinorError);
				weightGradients.accumulateProduct(inputMinorData, middleMinorError, MathCalculator.SERIAL);
				if (biasGradients != null) {
					for (int position = 0, size = biasGradients.getColumnSize(); position < size; position++) {
						float bias = middleMinorError.getSum(false);
						biasGradients.shiftValue(0, position, bias);
					}
				}

				// weightParameters.doProduct(middleError.transpose()).transpose()
				if (outerMajorError != null) {
					// TODO 使用累计的方式计算
					// TODO 需要锁机制,否则并发计算会导致Bug
					MathVector outerMinorError = CompositeVector.detachOf(CompositeVector.class.cast(outerMajorError), columnIndex * numberOfInputs + 1, (columnIndex + 1) * numberOfInputs + 1);
					outerMinorError.accumulateProduct(middleMinorError, weightParameters, true, MathCalculator.SERIAL);
				}
			}
		}

		float scale = 1F / numberOfInstances * inputData.getRowSize();
		weightGradients.scaleValues(scale);
		if (biasGradients != null) {
			biasGradients.scaleValues(scale);
		}
	}

}
