package com.jstarcraft.ai.math.algorithm.correlation.distance;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistanceTestCase;
import com.jstarcraft.ai.math.algorithm.correlation.MathCorrelation;
import com.jstarcraft.ai.math.structure.vector.DenseVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class LevenshteinDistanceTestCase extends AbstractDistanceTestCase {

    @Override
    protected MathCorrelation getCorrelation() {
        return new LevenshteinDistance();
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
    public void testDistance() {
        LevenshteinDistance distance = new LevenshteinDistance();

        Assert.assertEquals(0F, distance.getCoefficient(getVector("JStarCraft"), getVector("JStarCraft")), 0F);
        Assert.assertEquals(1F, distance.getCoefficient(getVector("JStarCraft"), getVector("LJStarCraft")), 0F);
        Assert.assertEquals(1F, distance.getCoefficient(getVector("JStarCraft"), getVector("JStarCraftR")), 0F);
        Assert.assertEquals(1F, distance.getCoefficient(getVector("LJStarCraft"), getVector("JStarCraft")), 0F);
        Assert.assertEquals(1F, distance.getCoefficient(getVector("JStarCraftR"), getVector("JStarCraft")), 0F);
        Assert.assertEquals(6F, distance.getCoefficient(getVector("JStarCraft"), getVector("Star")), 0F);
        Assert.assertEquals(8F, distance.getCoefficient(getVector("JStarCraft"), getVector("SC")), 0F);
        Assert.assertEquals(11F, distance.getCoefficient(getVector("JStarCraft"), getVector("HongZhaoHua")), 0F);
    }

}
