package be.tarsos.lsh.families;

import java.util.Arrays;
import java.util.Random;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistance;
import com.jstarcraft.ai.math.algorithm.correlation.distance.ManhattanDistance;

public class CityBlockHashFamily implements HashFamily {

    /**
     * 
     */
    private static final long serialVersionUID = -8926838846356323484L;
    private int dimensions;
    private int w;

    public CityBlockHashFamily(int w, int dimensions) {
        this.dimensions = dimensions;
        this.w = w;
    }

    @Override
    public HashFunction createHashFunction(Random rand) {
        return new CityBlockHash(rand, dimensions, w);
    }

    @Override
    public String combine(int[] hashes) {
        // return Arrays.hashCode(hashes);
        return Arrays.toString(hashes);
    }

    @Override
    public AbstractDistance createDistanceMeasure() {
        return new ManhattanDistance();
    }

}
