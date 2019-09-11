package com.jstarcraft.ai.data.attribute;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;

import com.jstarcraft.core.utility.RandomUtility;

public abstract class QuantityAttributeTestCase extends DataAttributeTestCase {

    abstract protected QuantityAttribute<Float> getQuantityAttribute();

    @Override
    public void testConvertValue() {
        int size = 1000;
        float[] datas = new float[size];
        for (int index = 0; index < size; index++) {
            datas[index] = index;
        }
        RandomUtility.shuffle(datas);
        QuantityAttribute<Float> attribute = getQuantityAttribute();
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(datas[index], attribute.convertData(datas[index]), 0F);
        }
        Assert.assertEquals(999F, attribute.getMaximum(), 0F);
        Assert.assertEquals(0F, attribute.getMinimum(), 0F);
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(datas[index], attribute.convertData(datas[index]), 0F);
        }
        Assert.assertEquals(999F, attribute.getMaximum(), 0F);
        Assert.assertEquals(0F, attribute.getMinimum(), 0F);
    }

    @Override
    public void testConcurrent() throws Exception {
        int size = 1000;
        float[] datas = new float[size];
        for (int index = 0; index < size; index++) {
            datas[index] = index;
        }
        RandomUtility.shuffle(datas);

        final int numberOfThread = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
        final CyclicBarrier barrier = new CyclicBarrier(numberOfThread + 1);
        QuantityAttribute<Float> attribute = getQuantityAttribute();
        for (int thread = 0; thread < numberOfThread; thread++) {
            executor.submit(() -> {
                try {
                    barrier.await();
                    for (int index = 0; index < size; index++) {
                        Assert.assertEquals(datas[index], attribute.convertData(datas[index]), 0F);
                    }
                    Assert.assertEquals(999F, attribute.getMaximum(), 0F);
                    Assert.assertEquals(0F, attribute.getMinimum(), 0F);
                    barrier.await();
                } catch (Exception exception) {
                    Assert.fail();
                }
            });
        }
        // 等待所有线程开始
        barrier.await();
        // 等待所有线程结束
        barrier.await();
    }

}
