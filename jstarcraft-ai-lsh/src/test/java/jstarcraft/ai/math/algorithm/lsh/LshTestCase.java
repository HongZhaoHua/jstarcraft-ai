package jstarcraft.ai.math.algorithm.lsh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.utility.StringUtility;

import be.tarsos.lsh.KeyVector;

public class LshTestCase {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<MathVector> dataset = new ArrayList<>(4);

    private MathVector left = new KeyVector("left", new float[] { 1F, 1F, 0F, 0F, 0F, 1F, 1F });

    private MathVector middle = new KeyVector("middle", new float[] { 0F, 0F, 1F, 1F, 1F, 0F, 0F });

    private MathVector right = new KeyVector("right", new float[] { 1F, 0F, 0F, 0F, 0F, 1F, 1F });

    private MathVector query = new KeyVector("query", new float[] { 0F, 1F, 1F, 1F, 1F, 0F, 0F });

    {
        dataset.add(left);
        dataset.add(middle);
        dataset.add(right);
        dataset.add(query);
    }

    private int[] signature(MathVector data, VectorHashFunction[] hashs) {
        int size = hashs.length;
        int[] signature = new int[size];
        for (int index = 0; index < size; index++) {
            VectorHashFunction hash = hashs[index];
            signature[index] = hash.hash(data);
        }
        return signature;
    }

    @Test
    public void testCosineHash() {
        Random random = new Random(0);
        VectorHashFunction[] hashs = new VectorHashFunction[3];
        for (int index = 0; index < 3; index++) {
            CosineHash hash = new CosineHash(random, 7);
            hashs[index] = hash;
        }

        if (logger.isDebugEnabled()) {
            for (MathVector data : dataset) {
                String message = StringUtility.format("CosineHash:{}", Arrays.toString(signature(data, hashs)));
                logger.debug(message);
            }
        }

        Assert.assertArrayEquals(signature(left, hashs), signature(right, hashs));
        Assert.assertArrayEquals(signature(middle, hashs), signature(query, hashs));
    }

    @Test
    public void testEuclideanHash() {
        Random random = new Random(0);
        VectorHashFunction[] hashs = new VectorHashFunction[3];
        for (int index = 0; index < 3; index++) {
            EuclideanHash hash = new EuclideanHash(random, 7, 7);
            hashs[index] = hash;
        }

        if (logger.isDebugEnabled()) {
            for (MathVector data : dataset) {
                String message = StringUtility.format("EuclideanHash:{}", Arrays.toString(signature(data, hashs)));
                logger.debug(message);
            }
        }

        Assert.assertArrayEquals(signature(left, hashs), signature(right, hashs));
        Assert.assertArrayEquals(signature(middle, hashs), signature(query, hashs));
    }

    @Test
    public void testManhattanHash() {
        Random random = new Random(0);
        VectorHashFunction[] hashs = new VectorHashFunction[3];
        for (int index = 0; index < 3; index++) {
            ManhattanHash hash = new ManhattanHash(random, 7, 7);
            hashs[index] = hash;
        }

        if (logger.isDebugEnabled()) {
            for (MathVector data : dataset) {
                String message = StringUtility.format("ManhattanHash:{}", Arrays.toString(signature(data, hashs)));
                logger.debug(message);
            }
        }

        Assert.assertArrayEquals(signature(left, hashs), signature(right, hashs));
        Assert.assertArrayEquals(signature(middle, hashs), signature(query, hashs));
    }

    @Test
    public void testMinHash() {
        Random random = new Random(0);
        VectorHashFunction[] hashs = new VectorHashFunction[3];
        for (int index = 0; index < 3; index++) {
            MinHash hash = new MinHash(random);
            hashs[index] = hash;
        }

        if (logger.isDebugEnabled()) {
            for (MathVector data : dataset) {
                String message = StringUtility.format("MinHash:{}", Arrays.toString(signature(data, hashs)));
                logger.debug(message);
            }
        }

        Assert.assertArrayEquals(signature(left, hashs), signature(right, hashs));
        Assert.assertArrayEquals(signature(middle, hashs), signature(query, hashs));
    }

}
