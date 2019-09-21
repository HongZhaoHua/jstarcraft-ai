package be.tarsos.lsh.families;

import java.util.Arrays;
import java.util.Random;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistance;
import com.jstarcraft.ai.math.algorithm.correlation.distance.EuclideanDistance;

public class EuclidianHashFamily implements HashFamily {
    /**
     * 
     */
    private static final long serialVersionUID = 3406464542795652263L;
    private final int dimensions;
    private int w;

    public EuclidianHashFamily(int w, int dimensions) {
        this.dimensions = dimensions;
        this.w = w;
    }

    @Override
    public HashFunction createHashFunction(Random rand) {
        return new EuclideanHash(rand, dimensions, w);
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
