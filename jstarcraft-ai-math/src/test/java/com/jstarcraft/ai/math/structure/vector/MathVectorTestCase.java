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
                MathVector left = new ArrayVector(3, new int[] { 0, 1, 2 }, new float[] { 0F, 2F, 0F });
                MathVector right = new ArrayVector(3, new int[] { 0, 1, 2 }, new float[] { 0F, 0F, 2F });
                // 法向量
                MathVector vector = new ArrayVector(3, new int[] { 0, 1, 2 }, new float[] { 0F, 0F, 0F });
                vector.crossProduct(left, right, MathCalculator.SERIAL);
                Assert.assertEquals(4F, Math.abs(vector.getNorm(2F, true)), 0F);

                vector.crossProduct(left, right, MathCalculator.PARALLEL);
                Assert.assertEquals(4F, Math.abs(vector.getNorm(2F, true)), 0F);
            }

            {
                // 测试稀疏的情况
                MathVector left = new ArrayVector(1, new int[] { 1 }, new float[] { 2F });
                MathVector right = new ArrayVector(1, new int[] { 2 }, new float[] { 2F });
                // 法向量
                MathVector vector = new ArrayVector(3, new int[] { 0, 1, 2 }, new float[] { 0F, 0F, 0F });
                vector.crossProduct(left, right, MathCalculator.SERIAL);
                Assert.assertEquals(4F, Math.abs(vector.getNorm(2F, true)), 0F);

                vector.crossProduct(left, right, MathCalculator.PARALLEL);
                Assert.assertEquals(4F, Math.abs(vector.getNorm(2F, true)), 0F);
            }
        });
        task.get();
    }

}
