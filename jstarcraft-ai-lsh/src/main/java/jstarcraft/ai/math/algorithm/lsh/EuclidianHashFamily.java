package jstarcraft.ai.math.algorithm.lsh;

import java.util.Arrays;
import java.util.Random;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistance;
import com.jstarcraft.ai.math.algorithm.correlation.distance.EuclideanDistance;

public class EuclidianHashFamily implements HashFamily {

    private final int dimensions;

    private int w;

    public EuclidianHashFamily(int w, int dimensions) {
        this.dimensions = dimensions;
        this.w = w;
    }

    @Override
    public HashFunction createHashFunction(Random random) {
        return new EuclideanHash(random, dimensions, w);
    }

    @Override
    public String combine(int[] hashes) {
        // return Arrays.hashCode(hashes);
        return Arrays.toString(hashes);
    }

    @Override
    public AbstractDistance createDistanceMeasure() {
        return new EuclideanDistance();
    }
}
