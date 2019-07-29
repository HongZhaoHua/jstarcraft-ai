package com.jstarcraft.ai.model.neuralnetwork.activation;

import java.util.concurrent.Future;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.activations.IActivation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.primitives.Pair;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentFactory;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.vector.DenseVector;
import com.jstarcraft.ai.modem.ModemCodec;
import com.jstarcraft.ai.utility.MathUtility;

public abstract class ActivationFunctionTestCase {

    private static DenseMatrix getMatrix(INDArray array) {
        DenseMatrix matrix = DenseMatrix.valueOf(array.rows(), array.columns());
        matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            scalar.setValue(array.getFloat(scalar.getRow(), scalar.getColumn()));
        });
        return matrix;
    }

    private static boolean equalMatrix(DenseMatrix matrix, INDArray array) {
        for (int row = 0; row < matrix.getRowSize(); row++) {
            for (int column = 0; column < matrix.getColumnSize(); column++) {
                if (!MathUtility.equal(matrix.getValue(row, column), array.getFloat(row, column))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean equalVector(DenseVector left, DenseVector right) {
        for (int index = 0; index < left.getElementSize(); index++) {
            if (left.getValue(index) != right.getValue(index)) {
                return false;
            }
        }
        return true;
    }

    protected abstract IActivation getOldFunction();

    protected abstract ActivationFunction getNewFunction();

    @Test
    public void testForward() throws Exception {
        EnvironmentContext context = EnvironmentFactory.getContext();
        Future<?> task = context.doTask(() -> {
            INDArray array = Nd4j.linspace(-2.5D, 2.0D, 10).reshape(5, 2);
            IActivation oldFunction = getOldFunction();
            INDArray value = oldFunction.getActivation(array.dup(), true);

            DenseMatrix input = getMatrix(array);
            DenseMatrix output = DenseMatrix.valueOf(input.getRowSize(), input.getColumnSize());
            ActivationFunction newFuction = getNewFunction();
            newFuction.forward(input, output);

            System.out.println(value);
            System.out.println(output);
            Assert.assertTrue(equalMatrix(output, value));

            DenseVector vector = DenseVector.valueOf(input.getColumnSize());
            for (int index = 0, size = input.getRowSize(); index < size; index++) {
                newFuction.forward(input.getRowVector(index), vector);
                Assert.assertTrue(equalVector(vector, output.getRowVector(index)));
            }
        });
        task.get();
    }

    @Test
    public void testBackward() throws Exception {
        EnvironmentContext context = EnvironmentFactory.getContext();
        Future<?> task = context.doTask(() -> {
            INDArray array = Nd4j.linspace(-2.5D, 2.0D, 10).reshape(5, 2);
            IActivation oldFunction = getOldFunction();
            INDArray epsilon = Nd4j.linspace(-2.5D, 2.5D, 10).reshape(5, 2);
            Pair<INDArray, INDArray> keyValue = oldFunction.backprop(array.dup(), epsilon);

            DenseMatrix input = getMatrix(array);
            DenseMatrix output = DenseMatrix.valueOf(input.getRowSize(), input.getColumnSize());
            DenseMatrix error = getMatrix(epsilon);
            ActivationFunction newFuction = getNewFunction();
            newFuction.backward(input, error, output);

            System.out.println(keyValue.getKey());
            System.out.println(output);
            Assert.assertTrue(equalMatrix(output, keyValue.getKey()));

            DenseVector vector = DenseVector.valueOf(input.getColumnSize());
            for (int index = 0, size = input.getRowSize(); index < size; index++) {
                newFuction.backward(input.getRowVector(index), error.getRowVector(index), vector);
                Assert.assertTrue(equalVector(vector, output.getRowVector(index)));
            }
        });
        task.get();
    }

    @Test
    public void testModel() {
        ActivationFunction oldModel = getNewFunction();
        for (ModemCodec codec : ModemCodec.values()) {
            byte[] data = codec.encodeModel(oldModel);
            ActivationFunction newModel = (ActivationFunction) codec.decodeModel(data);
            Assert.assertThat(newModel, CoreMatchers.equalTo(oldModel));
        }
    }

}
