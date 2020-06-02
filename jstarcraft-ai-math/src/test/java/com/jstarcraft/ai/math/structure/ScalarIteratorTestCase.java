package com.jstarcraft.ai.math.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.math.MathUtility;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.message.BoundaryMessage;
import com.jstarcraft.ai.math.structure.message.NormMessage;
import com.jstarcraft.ai.math.structure.message.SumMessage;
import com.jstarcraft.ai.math.structure.message.VarianceMessage;
import com.jstarcraft.ai.math.structure.vector.DenseVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.Float2FloatKeyValue;
import com.jstarcraft.core.utility.KeyValue;
import com.jstarcraft.core.utility.RandomUtility;

public class ScalarIteratorTestCase {

    @Test
    public void testBoundary() {
        MathMatrix matrix = DenseMatrix.valueOf(5, 10);
        matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            scalar.setValue(RandomUtility.randomFloat(1F));
        });

        {
            BoundaryMessage message = new BoundaryMessage(false);
            matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
                message.accumulateValue(scalar.getValue());
            });

            KeyValue<Float, Float> keyValue = matrix.getBoundary(false);

            Assert.assertEquals("最小值比较", message.getValue().getKey(), keyValue.getKey(), MathUtility.EPSILON);
            Assert.assertEquals("最大值比较", message.getValue().getValue(), keyValue.getValue(), MathUtility.EPSILON);
        }

        {
            BoundaryMessage message = new BoundaryMessage(true);
            matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
                message.accumulateValue(scalar.getValue());
            });

            KeyValue<Float, Float> keyValue = matrix.getBoundary(true);

            Assert.assertEquals("最小值比较", message.getValue().getKey(), keyValue.getKey(), MathUtility.EPSILON);
            Assert.assertEquals("最大值比较", message.getValue().getValue(), keyValue.getValue(), MathUtility.EPSILON);
        }
    }

    @Test
    public void testKurtosisAndSkewness() {
        MathVector vector = DenseVector.valueOf(5, new float[] { 4.5899916F, 4.5916805F, 4.5911174F, 4.588303F, 4.5877404F });
        Assert.assertEquals("峰度比较", -1.2815338F, vector.getKurtosis(vector.getSum(false)), MathUtility.EPSILON);
        Assert.assertEquals("偏度比较", -0.20210096F, vector.getSkewness(vector.getSum(false)), MathUtility.EPSILON);
    }

    private double getMedian(ArrayList<Integer> data) {
        int lenght = data.size();
        // 中位数
        double median = 0;
        Collections.sort(data);
        if (lenght % 2 == 0)
            median = (data.get((lenght - 1) / 2) + data.get(lenght / 2)) / 2D;
        else
            median = data.get(lenght / 2);
        return median;
    }

    @Test
    public void testMedian() {
        Stream.iterate(1, size -> size + 1).limit(100).forEach(size -> {
            MathVector vector = DenseVector.valueOf(size);
            ArrayList<Integer> data = new ArrayList<>(size);

            Stream.iterate(0, times -> times).limit(100).forEach(times -> {
                for (int index = 0; index < size; index++) {
                    int value = RandomUtility.randomInteger(-10, 10);
                    vector.setValue(index, value);
                    data.add(value);
                }
                Assert.assertEquals("中位数值比较", getMedian(data), vector.getMedian(false), MathUtility.EPSILON);
                data.clear();
            });
        });
    }

    @Test
    public void testNorm() {
        MathMatrix matrix = DenseMatrix.valueOf(5, 10);
        matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            scalar.setValue(RandomUtility.randomFloat(1F));
        });

        for (int index = 0; index < 10; index++) {
            NormMessage message = new NormMessage(index);
            matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
                message.accumulateValue(scalar.getValue());
            });

            if (index == 0) {
                Assert.assertEquals("范数值比较", message.getValue(), matrix.getNorm(index), MathUtility.EPSILON);
            } else {
                Assert.assertEquals("范数值比较", Math.pow(message.getValue(), 1D / message.getPower()), matrix.getNorm(index), MathUtility.EPSILON);
            }
        }
    }

    @Test
    public void testVariance() {
        MathMatrix matrix = DenseMatrix.valueOf(5, 10);
        matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            scalar.setValue(RandomUtility.randomFloat(1F));
        });

        float mean = matrix.getSum(false) / matrix.getElementSize();
        VarianceMessage message = new VarianceMessage(mean);
        matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            message.accumulateValue(scalar.getValue());
        });

        Float2FloatKeyValue keyValue = matrix.getVariance();

        Assert.assertEquals("平均值比较", message.getMean(), keyValue.getKey(), MathUtility.EPSILON);
        Assert.assertEquals("方差值比较", message.getValue(), keyValue.getValue(), MathUtility.EPSILON);
    }

    @Test
    public void testSum() {
        MathMatrix matrix = DenseMatrix.valueOf(5, 10);
        matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
            scalar.setValue(RandomUtility.randomFloat(1F));
        });

        {
            SumMessage message = new SumMessage(false);
            matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
                message.accumulateValue(scalar.getValue());
            });
            Assert.assertEquals("总值比较", message.getValue(), matrix.getSum(false), MathUtility.EPSILON);
        }

        {
            SumMessage message = new SumMessage(true);
            matrix.iterateElement(MathCalculator.SERIAL, (scalar) -> {
                message.accumulateValue(scalar.getValue());
            });
            Assert.assertEquals("总值比较", message.getValue(), matrix.getSum(true), MathUtility.EPSILON);
        }
    }

}
