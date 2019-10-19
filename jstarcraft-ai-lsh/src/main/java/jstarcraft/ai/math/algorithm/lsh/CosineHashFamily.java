package jstarcraft.ai.math.algorithm.lsh;

import java.util.Arrays;
import java.util.Random;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistance;
import com.jstarcraft.ai.math.algorithm.correlation.similarity.CosineSimilarity;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class CosineHashFamily implements LshHashFamily {

    private final int dimensions;

    public CosineHashFamily(int dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public VectorHashFunction getHashFunction(Random random) {
        return new CosineHashFunction(random, dimensions);
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
    public AbstractDistance createDistanceMeasure() {
        CosineSimilarity similarity = new CosineSimilarity();
        return new AbstractDistance() {

            @Override
            public float getCoefficient(MathVector leftVector, MathVector rightVector) {
                return 1F - similarity.getCoefficient(leftVector, rightVector);
            }

        };
    }
}
