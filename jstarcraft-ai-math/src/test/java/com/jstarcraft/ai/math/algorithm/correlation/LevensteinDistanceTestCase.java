package com.jstarcraft.ai.math.algorithm.correlation;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.math.algorithm.correlation.LevensteinDistance;
import com.jstarcraft.ai.math.algorithm.correlation.Correlation;
import com.jstarcraft.ai.math.structure.vector.DenseVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class LevensteinDistanceTestCase extends AbstractDistanceTestCase {

    @Override
    protected Correlation getCorrelation() {
        return new LevensteinDistance();
    }

    private MathVector getVector(String string) {
        int size = string.length();
        char[] characters = string.toCharArray();
        float[] data = new float[size];
        for (int index = 0; index < size; index++) {
            data[index] = characters[index];
        }
        MathVector vector = DenseVector.valueOf(size, data);
        return vector;
    }

    @Test
    public void testSimilarity() {
        LevensteinDistance distance = new LevensteinDistance();

        Assert.assertEquals(0F, distance.getCoefficient(getVector("JStarCraft"), getVector("JStarCraft"), 0F), 0F);
        Assert.assertEquals(0.09090909F, distance.getCoefficient(getVector("JStarCraft"), getVector("LJStarCraft"), 0F), 0F);
        Assert.assertEquals(0.09090909F, distance.getCoefficient(getVector("JStarCraft"), getVector("JStarCraftR"), 0F), 0F);
        Assert.assertEquals(0.09090909F, distance.getCoefficient(getVector("LJStarCraft"), getVector("JStarCraft"), 0F), 0F);
        Assert.assertEquals(0.09090909F, distance.getCoefficient(getVector("JStarCraftR"), getVector("JStarCraft"), 0F), 0F);
        Assert.assertEquals(0.6F, distance.getCoefficient(getVector("JStarCraft"), getVector("Star"), 0F), 0F);
        Assert.assertEquals(0.8F, distance.getCoefficient(getVector("JStarCraft"), getVector("SC"), 0F), 0F);
        Assert.assertEquals(1F, distance.getCoefficient(getVector("JStarCraft"), getVector("HongZhaoHua"), 0F), 0F);
    }

}
