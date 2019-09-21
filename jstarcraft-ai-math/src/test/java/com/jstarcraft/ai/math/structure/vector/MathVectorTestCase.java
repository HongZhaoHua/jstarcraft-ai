package com.jstarcraft.ai.math.structure.vector;

import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentFactory;
import com.jstarcraft.ai.math.structure.MathCalculator;

public class MathVectorTestCase {

    @Test
    public void testCrossProduct() throws Exception {
        EnvironmentContext context = EnvironmentFactory.getContext();
        Future<?> task = context.doTask(() -> {
            {
                // 测试稠密的情况
                MathVector left = new ArrayVector(3, new int[] { 0, 1, 2 }, new float[] { 0F, 3F, 0F });
                MathVector right = new ArrayVector(3, new int[] { 0, 1, 2 }, new float[] { 0F, 4F, 4F });
                // 法向量
                MathVector vector = new ArrayVector(3, new int[] { 0, 1, 2 }, new float[] { 0F, 0F, 0F });
                vector.crossProduct(left, right, MathCalculator.SERIAL);
                Assert.assertEquals(12F, vector.getSum(true), 0F);

                vector.crossProduct(left, right, MathCalculator.PARALLEL);
                Assert.assertEquals(12F, vector.getSum(true), 0F);
            }

            {
                // 测试稀疏的情况
                MathVector left = new ArrayVector(1, new int[] { 1 }, new float[] { 3F });
                MathVector right = new ArrayVector(2, new int[] { 1, 2 }, new float[] { 4F, 4F });
                // 法向量
                MathVector vector = new ArrayVector(3, new int[] { 0, 1, 2 }, new float[] { 0F, 0F, 0F });
                vector.crossProduct(left, right, MathCalculator.SERIAL);
                Assert.assertEquals(12F, vector.getSum(true), 0F);

                vector.crossProduct(left, right, MathCalculator.PARALLEL);
                Assert.assertEquals(12F, vector.getSum(true), 0F);
            }
        });
        task.get();
    }

}
