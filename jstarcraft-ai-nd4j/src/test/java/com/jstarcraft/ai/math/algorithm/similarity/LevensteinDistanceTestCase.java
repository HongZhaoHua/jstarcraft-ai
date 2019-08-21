package com.jstarcraft.ai.math.algorithm.similarity;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.math.structure.vector.DenseVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class LevensteinDistanceTestCase extends AbstractSimilarityTestCase {

    @Override
    protected boolean checkCorrelation(float correlation) {
        return correlation < 1.00001F;
    }

    @Override
    protected float getIdentical() {
        return 1F;
    }

    @Override
    protected Similarity getSimilarity() {
        return new LevensteinDistanceSimilarity();
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
        LevensteinDistanceSimilarity distance = new LevensteinDistanceSimilarity();

        Assert.assertEquals(1F, distance.getCorrelation(getVector("JStarCraft"), getVector("JStarCraft"), 0F), 0F);
        Assert.assertEquals(0.9090909F, distance.getCorrelation(getVector("JStarCraft"), getVector("LJStarCraft"), 0F), 0F);
        Assert.assertEquals(0.9090909F, distance.getCorrelation(getVector("JStarCraft"), getVector("JStarCraftR"), 0F), 0F);
        Assert.assertEquals(0.9090909F, distance.getCorrelation(getVector("LJStarCraft"), getVector("JStarCraft"), 0F), 0F);
        Assert.assertEquals(0.9090909F, distance.getCorrelation(getVector("JStarCraftR"), getVector("JStarCraft"), 0F), 0F);
        Assert.assertEquals(0.39999998F, distance.getCorrelation(getVector("JStarCraft"), getVector("Star"), 0F), 0F);
        Assert.assertEquals(0.19999999F, distance.getCorrelation(getVector("JStarCraft"), getVector("SC"), 0F), 0F);
        Assert.assertEquals(0.0F, distance.getCorrelation(getVector("JStarCraft"), getVector("HongZhaoHua"), 0F), 0F);
    }

}
