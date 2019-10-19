package jstarcraft.ai.math.algorithm.lsh;

import java.util.Arrays;
import java.util.Random;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistance;
import com.jstarcraft.ai.math.algorithm.correlation.similarity.JaccardIndexSimilarity;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class MinHashFamily implements LshHashFamily {

    public MinHashFamily() {
    }

    @Override
    public VectorHashFunction getHashFunction(Random random) {
        return new MinHashFunction(random);
    }

    @Override
    public String combine(int[] hashes) {
        // return Arrays.hashCode(hashes);
        return Arrays.toString(hashes);
    }

    @Override
    public AbstractDistance getDistance() {
        JaccardIndexSimilarity similarity = new JaccardIndexSimilarity();
        return new AbstractDistance() {

            @Override
            public float getCoefficient(MathVector leftVector, MathVector rightVector) {
                return 1F - similarity.getCoefficient(leftVector, rightVector);
            }

        };
    }
}
