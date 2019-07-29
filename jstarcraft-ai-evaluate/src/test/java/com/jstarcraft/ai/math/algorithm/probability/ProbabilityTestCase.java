package com.jstarcraft.ai.math.algorithm.probability;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.distribution.Distribution;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.modem.ModemCodec;
import com.jstarcraft.ai.utility.MathUtility;

public abstract class ProbabilityTestCase {

    protected static DenseMatrix getMatrix(INDArray array) {
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

    protected abstract void assertSample(Probability newFuction, Distribution oldFunction);

    protected abstract Distribution getOldFunction(int seed);

    protected abstract Probability getNewFunction(int seed);

    @Test
    public void testSample() {
        int seed = 1000;

        Distribution oldFunction = getOldFunction(seed);
        Probability<Number> newFuction = getNewFunction(seed);

        for (int index = 0; index < seed; index++) {
            newFuction.setSeed(index);
            oldFunction.reseedRandomGenerator(index);
            assertSample(newFuction, oldFunction);
        }

        Assert.assertThat(newFuction.getMaximum().doubleValue(), CoreMatchers.equalTo(oldFunction.getSupportUpperBound()));
        Assert.assertThat(newFuction.getMinimum().doubleValue(), CoreMatchers.equalTo(oldFunction.getSupportLowerBound()));
        Assert.assertThat(newFuction.inverseDistribution(1D).doubleValue(), CoreMatchers.equalTo(oldFunction.getSupportUpperBound()));
        Assert.assertThat(newFuction.inverseDistribution(0D).doubleValue(), CoreMatchers.equalTo(oldFunction.getSupportLowerBound()));
        Assert.assertThat(newFuction.cumulativeDistribution(newFuction.getMaximum()), CoreMatchers.equalTo(oldFunction.cumulativeProbability(oldFunction.getSupportUpperBound())));
        Assert.assertThat(newFuction.cumulativeDistribution(newFuction.getMinimum()), CoreMatchers.equalTo(oldFunction.cumulativeProbability(oldFunction.getSupportLowerBound())));
    }

    @Test
    public void testModem() {
        int seed = 1000;

        for (ModemCodec codec : ModemCodec.values()) {
            Probability<Number> oldFuction = getNewFunction(seed);
            byte[] data = codec.encodeModel(oldFuction);
            Probability<Number> newFuction = (Probability) codec.decodeModel(data);
            Assert.assertEquals(oldFuction.sample(), newFuction.sample());
        }
    }

}
