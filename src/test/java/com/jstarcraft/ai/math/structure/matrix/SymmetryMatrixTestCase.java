package com.jstarcraft.ai.math.structure.matrix;

import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.RandomUtility;

public class SymmetryMatrixTestCase extends MatrixTestCase {

	@Override
	protected SymmetryMatrix getRandomMatrix(int dimension) {
		SymmetryMatrix matrix = new SymmetryMatrix(dimension);
		matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			scalar.setValue(RandomUtility.randomInteger(dimension));
		});
		return matrix;
	}

	@Override
	protected SymmetryMatrix getZeroMatrix(int dimension) {
		SymmetryMatrix matrix = new SymmetryMatrix(dimension);
		return matrix;
	}

	@Test
	public void testProduct() throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {
			int dimension = 10;
			MathMatrix randomMatrix = getRandomMatrix(dimension);
			MathMatrix dataMatrix = getZeroMatrix(dimension);
			MathMatrix labelMatrix = DenseMatrix.valueOf(dimension, dimension);
			MathVector dataVector = dataMatrix.getRowVector(0);
			MathVector labelVector = labelMatrix.getRowVector(0);

			labelMatrix.dotProduct(randomMatrix, true, randomMatrix, false, MathCalculator.SERIAL);
			dataMatrix.dotProduct(randomMatrix, true, randomMatrix, false, MathCalculator.SERIAL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));
			dataMatrix.dotProduct(randomMatrix, true, randomMatrix, false, MathCalculator.PARALLEL);
			Assert.assertTrue(equalMatrix(dataMatrix, labelMatrix));

			MathVector leftVector = randomMatrix.getRowVector(RandomUtility.randomInteger(dimension));
			MathVector rightVector = randomMatrix.getRowVector(RandomUtility.randomInteger(dimension));
			labelVector.dotProduct(randomMatrix, false, rightVector, MathCalculator.SERIAL);
			dataVector.dotProduct(randomMatrix, false, rightVector, MathCalculator.SERIAL);
			Assert.assertTrue(equalVector(dataVector, labelVector));
			dataVector.dotProduct(randomMatrix, false, rightVector, MathCalculator.PARALLEL);
			Assert.assertTrue(equalVector(dataVector, labelVector));

			labelVector.dotProduct(leftVector, randomMatrix, false, MathCalculator.SERIAL);
			dataVector.dotProduct(leftVector, randomMatrix, false, MathCalculator.SERIAL);
			Assert.assertTrue(equalVector(dataVector, labelVector));
			dataVector.dotProduct(leftVector, randomMatrix, false, MathCalculator.PARALLEL);
			Assert.assertTrue(equalVector(dataVector, labelVector));
		});
		task.get();
	}

}
