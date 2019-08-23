package com.jstarcraft.ai.math.structure.tensor;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class TensorTestCase {

    @Test
    public void test() {
        int[] shape = new int[] { 3, 4, 5 };
        {
            Tensor tensor = new Tensor(shape, 1);
            System.out.println(Arrays.toString(tensor.shape()));
            System.out.println(Arrays.toString(tensor.mult()));
            System.out.println(tensor.size());
        }

        {
            INDArray array = Nd4j.create(shape, 'c');
            MathTensor tensor = new DenseTensor(true, shape);
            Assert.assertEquals("形状比较", Arrays.toString(array.shape()), Arrays.toString(tensor.getShape()));
            Assert.assertEquals("步幅比较", Arrays.toString(array.stride()), Arrays.toString(tensor.getStride()));
        }
        {
            INDArray array = Nd4j.create(shape, 'f');
            MathTensor tensor = new DenseTensor(false, shape);
            Assert.assertEquals("形状比较", Arrays.toString(array.shape()), Arrays.toString(tensor.getShape()));
            Assert.assertEquals("步幅比较", Arrays.toString(array.stride()), Arrays.toString(tensor.getStride()));
        }

    }

}
