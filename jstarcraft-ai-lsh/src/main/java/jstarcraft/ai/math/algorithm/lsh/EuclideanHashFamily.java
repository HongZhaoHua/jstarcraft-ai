package jstarcraft.ai.math.algorithm.lsh;

import java.util.Arrays;
import java.util.Random;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistance;
import com.jstarcraft.ai.math.algorithm.correlation.distance.EuclideanDistance;

public class EuclideanHashFamily implements LshHashFamily {

    private final int dimensions;

    private int w;

    public EuclideanHashFamily(int w, int dimensions) {
        this.dimensions = dimensions;
        this.w = w;
    }

    @Override
    public VectorHashFunction getHashFunction(Random random) {
        return new EuclideanHashFunction(random, dimensions, w);
    }

    @Override
    public String combine(int[] hashes) {
        // return Arrays.hashCode(hashes);
        return Arrays.toString(hashes);
    }

    @Override
    public AbstractDistance getDistance() {
        return new EuclideanDistance();
    }
}
