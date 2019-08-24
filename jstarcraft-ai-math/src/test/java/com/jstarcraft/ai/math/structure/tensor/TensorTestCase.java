package com.jstarcraft.ai.math.structure.tensor;

import java.util.Arrays;

import org.bytedeco.javacpp.FloatPointer;
import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.math.structure.MathCalculator;

public class TensorTestCase {

    @Test
    public void test() {
        int[] shape = new int[] { 3, 3 };
        float[] cData = new float[] { 0F, 8F, 4F, 5F, 3F, 5F, 2F, 3F, 6F };
        float[] fData = new float[] { 0F, 5F, 2F, 8F, 3F, 5F, 4F, 5F, 6F };
        {
//            Tensor tensor = new Tensor(shape, 1);
//            System.out.println(Arrays.toString(tensor.shape()));
//            System.out.println(Arrays.toString(tensor.mult()));
//            System.out.println(tensor.size());
        }

        {
            INDArray array = Nd4j.create(cData, shape, 'c');
            FloatPointer pointer = (FloatPointer) array.data().pointer();
            MathTensor tensor = new DenseTensor(true, shape, cData);
            Assert.assertEquals("形状比较", Arrays.toString(array.shape()), Arrays.toString(tensor.getShape()));
            Assert.assertEquals("步幅比较", Arrays.toString(array.stride()), Arrays.toString(tensor.getStride()));
            Assert.assertEquals(array.getFloat(new int[] { 0, 2 }), tensor.getValue(new int[] { 0, 2 }), 0F);
            Assert.assertEquals(array.getFloat(new int[] { 2, 0 }), tensor.getValue(new int[] { 2, 0 }), 0F);

            tensor.iterateElement(MathCalculator.SERIAL, (scalar) -> {
                Assert.assertEquals(scalar.getValue(), tensor.getValue(scalar.getIndexes()), 0F);
            });
        }
        {
            INDArray array = Nd4j.create(fData, shape, 'f');
            FloatPointer pointer = (FloatPointer) array.data().pointer();
            MathTensor tensor = new DenseTensor(false, shape, fData);
            Assert.assertEquals("形状比较", Arrays.toString(array.shape()), Arrays.toString(tensor.getShape()));
            Assert.assertEquals("步幅比较", Arrays.toString(array.stride()), Arrays.toString(tensor.getStride()));
            Assert.assertEquals(array.getFloat(new int[] { 0, 2 }), tensor.getValue(new int[] { 0, 2 }), 0F);
            Assert.assertEquals(array.getFloat(new int[] { 2, 0 }), tensor.getValue(new int[] { 2, 0 }), 0F);

            tensor.iterateElement(MathCalculator.SERIAL, (scalar) -> {
                Assert.assertEquals(scalar.getValue(), tensor.getValue(scalar.getIndexes()), 0F);
            });
        }

    }

}
