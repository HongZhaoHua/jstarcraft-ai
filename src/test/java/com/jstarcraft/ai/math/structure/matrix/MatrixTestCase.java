package com.jstarcraft.ai.math.structure.matrix;

import java.util.concurrent.Future;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.MockMessage;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.ai.math.structure.vector.VectorScalar;
import com.jstarcraft.ai.model.ModelCodec;
import com.jstarcraft.ai.utility.MathUtility;
import com.jstarcraft.core.utility.RandomUtility;
import com.jstarcraft.core.utility.StringUtility;

public abstract class MatrixTestCase {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected abstract MathMatrix getRandomMatrix(int dimension);

	protected abstract MathMatrix getZeroMatrix(int dimension);

	protected static boolean equalMatrix(MathMatrix left, MathMatrix right) {
		for (MatrixScalar term : left) {
			if (!MathUtility.equal(term.getValue(), right.getValue(term.getRow(), term.getColumn()))) {
				return false;
			}
		}
		return true;
	}

	protected static boolean equalVector(MathVector left, MathVector right) {
		for (VectorScalar term : left) {
			if (!MathUtility.equal(term.getValue(), right.getValue(term.getIndex()))) {
				return false;
			}
		}
		return true;
	}

	@Test
	public void testCalculate() throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {
			int dimension = 10;
			MathMatrix dataMatrix = getRandomMatrix(dimension);

			{
				MockMessage oldMessage = new MockMessage();
				MockMessage newMessage = new MockMessage();
				dataMatrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
					try {
						Thread.sleep(0L);
						oldMessage.accumulateValue(scalar.getValue());
					} catch (Exception exception) {
					}
				});
				Assert.assertThat(oldMessage.getValue(), CoreMatchers.equalTo(dataMatrix.getSum(false)));
				dataMatrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
					try {
						Thread.sleep(0L);
						scalar.shiftValue(1F);
						newMessage.accumulateValue(scalar.getValue());
					} catch (Exception exception) {
					}
				});
				Assert.assertThat(newMessage.getValue(), CoreMatchers.equalTo(dataMatrix.getSum(false)));
				Assert.assertTrue(dataMatrix.getElementSize() == (newMessage.getValue() - oldMessage.getValue()));
			}

			{
				MockMessage oldMessage = new MockMessage();
				MockMessage newMessage = new MockMessage();
				dataMatrix.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
					try {
						Thread.sleep(0L);
						oldMessage.accumulateValue(scalar.getValue());
					} catch (Exception exception) {
					}
				});
				Assert.assertThat(oldMessage.getValue(), CoreMatchers.equalTo(dataMatrix.getSum(false)));
				dataMatrix.iterateElement(MathCalculator.PARALLEL, (scalar) -> {
					try {
						Thread.sleep(0L);
						scalar.shiftValue(1F);
						newMessage.accumulateValue(scalar.getValue());
					} catch (Exception exception) {
					}
				});
				Assert.assertThat(newMessage.getValue(), CoreMatchers.equalTo(dataMatrix.getSum(false)));
				Assert.assertTrue(dataMatrix.getElementSize() == (newMessage.getValue() - oldMessage.getValue()));
			}
		});
		task.get();
	}

	@Test
	public void testProduct() throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {
			int dimension = 10;
			MathMatrix leftMatrix = getRandomMatrix(dimension);
			MathMatrix rightMatrix = getRandomMatrix(dimension);
			MathMatrix dataMatrix = getZeroMatrix(dimension);
			MathMatrix labelMatrix = DenseMatrix.valueOf(dimension, dimension);
			MathVector dataVector = dataMatrix.getRowVector(0);
			MathVector labelVector = labelMatrix.getRowVector(0);

			labelMatrix.dotProduct(leftMatrix, false, rightMatrix, true, MathCalculator.SERIAL);
			dataMatrix.dotProduct(leftMatrix, false, rightMatrix, true, MathCalculator.SERIAL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));
			dataMatrix.dotProduct(leftMatrix, false, rightMatrix, true, MathCalculator.PARALLEL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));

			MathVector leftVector = leftMatrix.getRowVector(RandomUtility.randomInteger(dimension));
			MathVector rightVector = rightMatrix.getRowVector(RandomUtility.randomInteger(dimension));
			labelMatrix.dotProduct(leftVector, rightVector, MathCalculator.SERIAL);
			dataMatrix.dotProduct(leftVector, rightVector, MathCalculator.SERIAL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));
			dataMatrix.dotProduct(leftVector, rightVector, MathCalculator.PARALLEL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));

			labelVector.dotProduct(leftMatrix, false, rightVector, MathCalculator.SERIAL);
			dataVector.dotProduct(leftMatrix, false, rightVector, MathCalculator.SERIAL);
			Assert.assertTrue(equalVector(dataVector, labelVector));
			dataVector.dotProduct(leftMatrix, false, rightVector, MathCalculator.PARALLEL);
			Assert.assertTrue(equalVector(dataVector, labelVector));

			labelVector.dotProduct(leftVector, rightMatrix, false, MathCalculator.SERIAL);
			dataVector.dotProduct(leftVector, rightMatrix, false, MathCalculator.SERIAL);
			Assert.assertTrue(equalVector(dataVector, labelVector));
			dataVector.dotProduct(leftVector, rightMatrix, false, MathCalculator.PARALLEL);
			Assert.assertTrue(equalVector(dataVector, labelVector));

			// 利用转置乘运算的对称性
			dataMatrix = new SymmetryMatrix(dimension);
			labelMatrix.dotProduct(leftMatrix, true, leftMatrix, false, MathCalculator.SERIAL);
			dataMatrix.dotProduct(leftMatrix, true, leftMatrix, false, MathCalculator.SERIAL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));
		});
		task.get();
	}

	@Test
	public void testSize() throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {
			int dimension = 10;
			MathMatrix dataMatrix = getRandomMatrix(dimension);

			Assert.assertThat(dataMatrix.getKnownSize() + dataMatrix.getUnknownSize(), CoreMatchers.equalTo(dataMatrix.getRowSize() * dataMatrix.getColumnSize()));

			int elementSize = 0;
			float sumValue = 0F;
			for (MatrixScalar term : dataMatrix) {
				elementSize++;
				sumValue += term.getValue();
			}
			Assert.assertThat(elementSize, CoreMatchers.equalTo(dataMatrix.getElementSize()));
			Assert.assertThat(sumValue, CoreMatchers.equalTo(dataMatrix.getSum(false)));
		});
		task.get();
	}

	@Test
	public void testSum() throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {
			int dimension = 10;
			MathMatrix dataMatrix = getRandomMatrix(dimension);

			float oldSum = dataMatrix.getSum(false);
			dataMatrix.scaleValues(2F);
			float newSum = dataMatrix.getSum(false);
			Assert.assertThat(newSum, CoreMatchers.equalTo(oldSum * 2F));

			oldSum = newSum;
			dataMatrix.shiftValues(1F);
			newSum = dataMatrix.getSum(false);
			Assert.assertThat(newSum, CoreMatchers.equalTo(oldSum + dataMatrix.getElementSize()));

			dataMatrix.setValues(0F);
			newSum = dataMatrix.getSum(false);
			Assert.assertThat(newSum, CoreMatchers.equalTo(0F));
		});
		task.get();
	}

	@Test
	public void testCodec() throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {
			// 维度设置为100,可以测试编解码的效率.
			int dimension = 100;
			MathMatrix oldMatrix = getRandomMatrix(dimension);

			for (ModelCodec codec : ModelCodec.values()) {
				long encodeInstant = System.currentTimeMillis();
				byte[] data = codec.encodeModel(oldMatrix);
				String encodeMessage = StringUtility.format("编码{}数据的时间:{}毫秒", codec, System.currentTimeMillis() - encodeInstant);
				logger.info(encodeMessage);
				long decodeInstant = System.currentTimeMillis();
				MathMatrix newMatrix = (MathMatrix) codec.decodeModel(data);
				String decodeMessage = StringUtility.format("解码{}数据的时间:{}毫秒", codec, System.currentTimeMillis() - decodeInstant);
				logger.info(decodeMessage);
				Assert.assertThat(newMatrix, CoreMatchers.equalTo(oldMatrix));
			}
		});
		task.get();
	}

	@Test
	public void testFourArithmeticOperation() throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {
			RandomUtility.setSeed(0L);
			int dimension = 10;
			MathMatrix dataMatrix = getZeroMatrix(dimension);
			dataMatrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
				scalar.setValue(RandomUtility.randomFloat(10F));
			});
			MathMatrix copyMatrix = getZeroMatrix(dimension);
			float sum = dataMatrix.getSum(false);

			copyMatrix.copyMatrix(dataMatrix, false);
			Assert.assertThat(copyMatrix.getSum(false), CoreMatchers.equalTo(sum));

			dataMatrix.subtractMatrix(copyMatrix, false);
			Assert.assertThat(dataMatrix.getSum(false), CoreMatchers.equalTo(0F));

			dataMatrix.addMatrix(copyMatrix, false);
			Assert.assertThat(dataMatrix.getSum(false), CoreMatchers.equalTo(sum));

			dataMatrix.divideMatrix(copyMatrix, false);
			Assert.assertThat(dataMatrix.getSum(false), CoreMatchers.equalTo(dataMatrix.getElementSize() + 0F));

			dataMatrix.multiplyMatrix(copyMatrix, false);
			Assert.assertThat(dataMatrix.getSum(false), CoreMatchers.equalTo(sum));
		});
		task.get();
	}

	@Test
	public void testPerformance() throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {

		});
		task.get();
		// 性能测试
	}

}
