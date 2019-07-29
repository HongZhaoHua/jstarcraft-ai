package com.jstarcraft.ai.model.neuralnetwork.layer;

import java.util.Map;

import org.apache.commons.math3.util.FastMath;
import org.nd4j.linalg.api.ndarray.INDArray;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.MatrixScalar;
import com.jstarcraft.ai.math.structure.matrix.Nd4jMatrix;
import com.jstarcraft.ai.model.neuralnetwork.activation.ActivationFunction;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 权重层
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
public class WeightLayer extends AbstractLayer {

	public final static String WEIGHT_KEY = "weight";

	public final static String BIAS_KEY = "bias";

	protected WeightLayer() {
		super();
	}

	public WeightLayer(int numberOfInputs, int numberOfOutputs, MathCache factory, Map<String, ParameterConfigurator> configurators, ActivationFunction function) {
		super(numberOfInputs, numberOfOutputs, configurators, function);

		if (!this.configurators.containsKey(WEIGHT_KEY)) {
			String message = StringUtility.format("参数{}配置缺失.", WEIGHT_KEY);
			throw new IllegalArgumentException(message);
		}

		MathMatrix weightParameter = factory.makeMatrix(numberOfInputs, numberOfOutputs);
		configurators.get(WEIGHT_KEY).getFactory().setValues(weightParameter);
		this.parameters.put(WEIGHT_KEY, weightParameter);
		MathMatrix weightGradient = factory.makeMatrix(numberOfInputs, numberOfOutputs);
		this.gradients.put(WEIGHT_KEY, weightGradient);

		if (this.configurators.containsKey(BIAS_KEY)) {
			MathMatrix biasParameter = factory.makeMatrix(1, numberOfOutputs);
			configurators.get(BIAS_KEY).getFactory().setValues(biasParameter);
			this.parameters.put(BIAS_KEY, biasParameter);
			MathMatrix biasGradient = factory.makeMatrix(1, numberOfOutputs);
			this.gradients.put(BIAS_KEY, biasGradient);
		}
	}

	@Override
	public float calculateL1Norm() {
		float l1Sum = 0F;

		Float weightRegularization = configurators.get(WEIGHT_KEY).getL1Regularization();
		MathMatrix weightParameters = parameters.get(WEIGHT_KEY);
		if (weightRegularization != null && weightParameters != null) {
			if (weightParameters instanceof Nd4jMatrix) {
				INDArray array = Nd4jMatrix.class.cast(weightParameters).getArray();
				float norm = array.norm1Number().floatValue();
				l1Sum += weightRegularization * norm;
			} else {
				float norm = 0F;
				for (MatrixScalar term : weightParameters) {
					norm += FastMath.abs(term.getValue());
				}
				l1Sum += weightRegularization * norm;
			}
		}

		if (this.configurators.containsKey(BIAS_KEY)) {
			Float biasRegularization = configurators.get(BIAS_KEY).getL1Regularization();
			MathMatrix biasParameters = parameters.get(BIAS_KEY);
			if (biasRegularization != null && biasParameters != null) {
				if (biasParameters instanceof Nd4jMatrix) {
					INDArray array = Nd4jMatrix.class.cast(biasParameters).getArray();
					float norm = array.norm1Number().floatValue();
					l1Sum += biasRegularization * norm;
				} else {
					float norm = 0F;
					for (MatrixScalar term : biasParameters) {
						norm += FastMath.abs(term.getValue());
					}
					l1Sum += biasRegularization * norm;
				}
			}
		}

		return l1Sum;
	}

	@Override
	public float calculateL2Norm() {
		float l2Sum = 0F;

		Float weightRegularization = configurators.get(WEIGHT_KEY).getL2Regularization();
		MathMatrix weightParameters = parameters.get(WEIGHT_KEY);
		if (weightRegularization != null && weightParameters != null) {
			if (weightParameters instanceof Nd4jMatrix) {
				INDArray array = Nd4jMatrix.class.cast(weightParameters).getArray();
				float norm = array.norm2Number().floatValue();
				l2Sum += 0.5F * weightRegularization * norm;
			} else {
				float norm = 0F;
				for (MatrixScalar term : weightParameters) {
					norm += term.getValue() * term.getValue();
				}
				l2Sum += 0.5F * weightRegularization * norm;
			}
		}

		if (this.configurators.containsKey(BIAS_KEY)) {
			Float biasRegularization = configurators.get(BIAS_KEY).getL2Regularization();
			MathMatrix biasParameters = parameters.get(BIAS_KEY);
			if (biasRegularization != null && biasParameters != null) {
				if (biasParameters instanceof Nd4jMatrix) {
					INDArray array = Nd4jMatrix.class.cast(biasParameters).getArray();
					float norm = array.norm2Number().floatValue();
					l2Sum += 0.5F * biasRegularization * norm;
				} else {
					float norm = 0F;
					for (MatrixScalar term : biasParameters) {
						norm += term.getValue() * term.getValue();
					}
					l2Sum += 0.5F * biasRegularization * norm;
				}
			}
		}

		return l2Sum;
	}

	@Override
	public void doForward() {
		MathMatrix weightParameters = parameters.get(WEIGHT_KEY);
		MathMatrix biasParameters = parameters.get(BIAS_KEY);

		MathMatrix inputData = getMatrix(inputKeyValue.getKey());
		MathMatrix middleData = getMatrix(middleKeyValue.getKey());
		MathMatrix outputData = getMatrix(outputKeyValue.getKey());

		middleData.dotProduct(inputData, false, weightParameters, false, MathCalculator.PARALLEL);
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

		MathMatrix inputData = getMatrix(inputKeyValue.getKey());
		MathMatrix middleData = getMatrix(middleKeyValue.getKey());
		MathMatrix outputData = getMatrix(outputKeyValue.getKey());

		MathMatrix innerError = getMatrix(outputKeyValue.getValue());
		MathMatrix middleError = getMatrix(middleKeyValue.getValue());
		MathMatrix outerError = getMatrix(inputKeyValue.getValue());

		// 计算梯度
		function.backward(middleData, innerError, middleError);

		weightGradients.dotProduct(inputData, true, middleError, false, MathCalculator.PARALLEL);
		if (biasGradients != null) {
			for (int columnIndex = 0, columnSize = biasGradients.getColumnSize(); columnIndex < columnSize; columnIndex++) {
				float bias = middleError.getColumnVector(columnIndex).getSum(false);
				biasGradients.setValue(0, columnIndex, bias);
			}
		}

		// weightParameters.doProduct(middleError.transpose()).transpose()
		if (outerError != null) {
			// TODO 使用累计的方式计算
			// TODO 需要锁机制,否则并发计算会导致Bug
			outerError.accumulateProduct(middleError, false, weightParameters, true, MathCalculator.PARALLEL);
			// outerError.calculate((row, column, value, message) -> {
			// return value +
			// StructureUtility.innerProduct(weightParameters.getRowVector(column),
			// middleError.getRowVector(row));
			// }, null, Calculator.getDefault(outerError));
			// CountDownLatch latch = new
			// CountDownLatch(weightParameters.getRowSize());
			// for (int row = 0; row < weightParameters.getRowSize(); row++) {
			// int rowIndex = row;
			// DenseMatrix.EXECUTOR.execute(() -> {
			// for (int column = 0; column < middleError.getRowSize(); column++)
			// {
			// double value = 0D;
			// for (int index = 0; index < weightParameters.getColumnSize();
			// index++) {
			// value += weightParameters.getValue(rowIndex, index) *
			// middleError.getValue(column, index);
			// }
			// outerError.setValue(column, rowIndex, value);
			// }
			// latch.countDown();
			// });
			// }
			// try {
			// latch.await();
			// } catch (Exception exception) {
			// throw new RuntimeException(exception);
			// }
		}
	}

}
