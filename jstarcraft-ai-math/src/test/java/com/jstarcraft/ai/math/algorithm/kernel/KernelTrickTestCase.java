package com.jstarcraft.ai.math.algorithm.kernel;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.ai.math.MathUtility;
import com.jstarcraft.ai.math.algorithm.correlation.distance.KernelDistance;
import com.jstarcraft.ai.math.algorithm.correlation.similarity.KernelSimilarity;
import com.jstarcraft.ai.math.structure.vector.DenseVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.RandomUtility;

public abstract class KernelTrickTestCase {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract KernelTrick getKernelTrick();

    @Test
    public void testKernelTrick() {
        MathVector leftVector = DenseVector.valueOf(1000);
        MathVector rightVector = DenseVector.valueOf(1000);
        for (int index = 0; index < 1000; index++) {
            leftVector.setValue(index, RandomUtility.randomFloat(0.005F));
            rightVector.setValue(index, RandomUtility.randomFloat(0.005F));
        }

        KernelTrick kernel = getKernelTrick();
        KernelDistance distance = new KernelDistance(kernel);
        KernelSimilarity similarity = new KernelSimilarity(kernel);

        Assert.assertTrue(kernel.calculate(leftVector, leftVector) > kernel.calculate(leftVector, rightVector));
        Assert.assertTrue(kernel.calculate(rightVector, rightVector) > kernel.calculate(leftVector, rightVector));

        Assert.assertEquals(0F, distance.getCoefficient(leftVector, leftVector), MathUtility.EPSILON);
        Assert.assertEquals(0F, distance.getCoefficient(rightVector, rightVector), MathUtility.EPSILON);
        Assert.assertTrue(distance.getCoefficient(leftVector, rightVector) > 0F);

        Assert.assertEquals(1F, similarity.getCoefficient(leftVector, leftVector), MathUtility.EPSILON);
        Assert.assertEquals(1F, similarity.getCoefficient(rightVector, rightVector), MathUtility.EPSILON);
        Assert.assertTrue(similarity.getCoefficient(leftVector, rightVector) < 1F);
    }

}
