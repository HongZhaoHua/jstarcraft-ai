package com.jstarcraft.ai.neuralnetwork.vertex;

import java.util.Map;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.ColumnCompositeMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.message.SumMessage;
import com.jstarcraft.ai.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.ai.neuralnetwork.layer.ParameterConfigurator;
import com.jstarcraft.ai.neuralnetwork.layer.WeightLayer;
import com.jstarcraft.core.utility.KeyValue;

/**
 * 共享层
 * 
 * @author Birdy
 *
 */
public class ShareLayer extends WeightLayer {

	private int numberOfShares;

	private MathMatrix weightCache;

	public ShareLayer(int numberOfInputs, int numberOfOutputs, int numberOfShares, MathCache factory, Map<String, ParameterConfigurator> configurators, Mode mode, ActivationFunction function) {
		super(numberOfInputs, numberOfOutputs, factory, configurators, mode, function);
		this.numberOfShares = numberOfShares;
		MathMatrix weightGradients = gradients.get(WEIGHT_KEY);
		this.weightCache = factory.makeMatrix(weightGradients.getRowSize(), weightGradients.getColumnSize());
	}

	private static MathMatrix[] getMatrixes(MathCache factory, int rowSize, int columnSize, int numberOfShares) {
		MathMatrix[] matrixes = new MathMatrix[numberOfShares];
		for (int randomIndex = 0; randomIndex < numberOfShares; randomIndex++) {
			matrixes[randomIndex] = factory.makeMatrix(rowSize, columnSize);
		}
		return matrixes;
	}

	@Override
	public void doCache(MathCache factory, KeyValue<MathMatrix, MathMatrix> samples) {
		inputKeyValue = samples;
		int rowSize = inputKeyValue.getKey().getRowSize();
		int columnSize = inputKeyValue.getKey().getColumnSize();

		// 检查维度
		if (columnSize != numberOfInputs * numberOfShares) {
			throw new IllegalArgumentException();
		}

		columnSize = numberOfOutputs * numberOfShares;
		middleKeyValue = new KeyValue<>(null, null);
		outputKeyValue = new KeyValue<>(null, null);

		// TODO 此处需要改为CompositeMatrix
		MathMatrix middleData = ColumnCompositeMatrix.attachOf(getMatrixes(factory, rowSize, numberOfOutputs, numberOfShares));
		middleKeyValue.setKey(middleData);
		MathMatrix middleError = ColumnCompositeMatrix.attachOf(getMatrixes(factory, rowSize, numberOfOutputs, numberOfShares));
		middleKeyValue.setValue(middleError);

		MathMatrix outputData = ColumnCompositeMatrix.attachOf(getMatrixes(factory, rowSize, numberOfOutputs, numberOfShares));
		outputKeyValue.setKey(outputData);
		MathMatrix innerError = ColumnCompositeMatrix.attachOf(getMatrixes(factory, rowSize, numberOfOutputs, numberOfShares));
		outputKeyValue.setValue(innerError);
	}

	@Override
	public void doForward() {
		MathMatrix weightParameters = parameters.get(WEIGHT_KEY);
		MathMatrix biasParameters = parameters.get(BIAS_KEY);

		for (int shareIndex = 0; shareIndex < numberOfShares; shareIndex++) {
			MathMatrix inputData = ColumnCompositeMatrix.detachOf(ColumnCompositeMatrix.class.cast(inputKeyValue.getKey()), shareIndex * numberOfInputs, (shareIndex + 1) * numberOfInputs);
			MathMatrix middleData = ColumnCompositeMatrix.detachOf(ColumnCompositeMatrix.class.cast(middleKeyValue.getKey()), shareIndex * numberOfOutputs, (shareIndex + 1) * numberOfOutputs);
			MathMatrix outputData = ColumnCompositeMatrix.detachOf(ColumnCompositeMatrix.class.cast(outputKeyValue.getKey()), shareIndex * numberOfOutputs, (shareIndex + 1) * numberOfOutputs);

			middleData.dotProduct(inputData, false, weightParameters, false, MathCalculator.PARALLEL);
			if (biasParameters != null) {
				for (int columnIndex = 0, columnSize = middleData.getColumnSize(); columnIndex < columnSize; columnIndex++) {
					float bias = biasParameters.getValue(0, columnIndex);
					middleData.getColumnVector(columnIndex).iterateElement(MathCalculator.SERIAL, (scalar) -> {
						scalar.setValue(scalar.getValue() + bias);
					});
				}
			}

			function.forward(middleData, outputData);
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

		for (int partIndex = 0; partIndex < numberOfShares; partIndex++) {
			MathMatrix inputData = ColumnCompositeMatrix.detachOf(ColumnCompositeMatrix.class.cast(inputKeyValue.getKey()), partIndex * numberOfInputs, (partIndex + 1) * numberOfInputs);
			MathMatrix middleData = ColumnCompositeMatrix.detachOf(ColumnCompositeMatrix.class.cast(middleKeyValue.getKey()), partIndex * numberOfOutputs, (partIndex + 1) * numberOfOutputs);
			MathMatrix outputData = ColumnCompositeMatrix.detachOf(ColumnCompositeMatrix.class.cast(outputKeyValue.getKey()), partIndex * numberOfOutputs, (partIndex + 1) * numberOfOutputs);

			MathMatrix innerError = ColumnCompositeMatrix.detachOf(ColumnCompositeMatrix.class.cast(outputKeyValue.getValue()), partIndex * numberOfOutputs, (partIndex + 1) * numberOfOutputs);
			MathMatrix middleError = ColumnCompositeMatrix.detachOf(ColumnCompositeMatrix.class.cast(middleKeyValue.getValue()), partIndex * numberOfOutputs, (partIndex + 1) * numberOfOutputs);
			MathMatrix outerError = ColumnCompositeMatrix.detachOf(ColumnCompositeMatrix.class.cast(inputKeyValue.getValue()), partIndex * numberOfInputs, (partIndex + 1) * numberOfInputs);

			// 计算梯度
			function.backward(middleData, innerError, middleError);

			weightCache.dotProduct(inputData, true, middleError, false, MathCalculator.PARALLEL);
			weightGradients.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				int row = scalar.getRow();
				int column = scalar.getColumn();
				float value = scalar.getValue();
				scalar.setValue(value += weightCache.getValue(row, column));
			});
			if (biasGradients != null) {
				SumMessage bias = new SumMessage(false);
				for (int columnIndex = 0, columnSize = biasGradients.getColumnSize(); columnIndex < columnSize; columnIndex++) {
					bias.accumulateValue(-bias.getValue());
					middleError.getColumnVector(columnIndex).iterateElement(MathCalculator.SERIAL, (scalar) -> {
						bias.accumulateValue(scalar.getValue());
					});
					biasGradients.shiftValue(0, columnIndex, bias.getValue());
				}
			}

			// weightParameters.doProduct(middleError.transpose()).transpose()
			if (outerError != null) {
				// TODO 使用累计的方式计算
				// TODO 需要锁机制,否则并发计算会导致Bug
				outerError.accumulateProduct(middleError, false, weightParameters, true, MathCalculator.PARALLEL);
			}
		}

		weightGradients.scaleValues(1F / numberOfShares);
		if (biasGradients != null) {
			biasGradients.scaleValues(1F / numberOfShares);
		}
	}

}
