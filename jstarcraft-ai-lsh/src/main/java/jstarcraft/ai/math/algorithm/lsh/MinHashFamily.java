package jstarcraft.ai.math.algorithm.lsh;

import java.util.Random;

import com.jstarcraft.ai.math.algorithm.correlation.similarity.JaccardIndexSimilarity;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class MinHashFamily implements LshHashFamily {

    private static final JaccardIndexSimilarity similarity = new JaccardIndexSimilarity();

    public MinHashFamily() {
    }

    @Override
    public VectorHashFunction getHashFunction(Random random) {
        return new MinHashFunction(random);
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        return 1F - similarity.getCoefficient(leftVector, rightVector);
    }
}
