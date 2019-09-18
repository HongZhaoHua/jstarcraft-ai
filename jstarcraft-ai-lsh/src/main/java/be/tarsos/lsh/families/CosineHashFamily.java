package be.tarsos.lsh.families;

import java.util.Arrays;
import java.util.Random;

public class CosineHashFamily implements HashFamily {

    /**
     * 
     */
    private static final long serialVersionUID = 7678152513757669089L;
    private final int dimensions;

    public CosineHashFamily(int dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public HashFunction createHashFunction(Random rand) {
        return new CosineHash(rand, dimensions);
    }

    @Override
    public String combine(int[] hashes) {
        // Treat the hashes as a series of bits.
        // They are either zero or one, the index
        // represents the value.
        // int result = 0;
        // factor holds the power of two.
        // int factor = 1;
        // for(int i = 0 ; i < hashes.length ; i++){
        // result += hashes[i] == 0 ? 0 : factor;
        // factor *= 2;
        // }
        // return result;
        return Arrays.toString(hashes);
    }

    @Override
    public DistanceMeasure createDistanceMeasure() {
        return new CosineDistance();
    }
}
