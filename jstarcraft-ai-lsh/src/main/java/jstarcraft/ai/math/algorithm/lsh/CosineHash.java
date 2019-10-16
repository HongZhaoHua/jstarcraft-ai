package jstarcraft.ai.math.algorithm.lsh;

import java.util.Random;

import com.jstarcraft.ai.math.structure.DefaultScalar;
import com.jstarcraft.ai.math.structure.MathScalar;
import com.jstarcraft.ai.math.structure.vector.MathVector;

import be.tarsos.lsh.KeyVector;

public class CosineHash implements HashFunction {

    final MathVector randomProjection;

    public CosineHash(Random random, int dimensions) {
        randomProjection = new KeyVector("random", dimensions);
        for (int dimension = 0; dimension < dimensions; dimension++) {
            // mean 0
            // standard deviation 1.0
            float value = (float) random.nextGaussian();
            randomProjection.setValue(dimension, value);
        }
    }

    @Override
    public int hash(MathVector vector) {
        MathScalar scalar = DefaultScalar.getInstance();
        // calculate the dot product.
        float hash = scalar.dotProduct(vector, randomProjection).getValue();
        // returns a 'bit' encoded as an integer.
        // 1 when positive or zero, 0 otherwise.
        return hash > 0 ? 1 : 0;
    }
}
