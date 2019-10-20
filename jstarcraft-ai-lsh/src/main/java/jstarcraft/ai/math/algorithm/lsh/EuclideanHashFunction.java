package jstarcraft.ai.math.algorithm.lsh;

import java.util.Random;

import com.jstarcraft.ai.math.structure.DefaultScalar;
import com.jstarcraft.ai.math.structure.MathScalar;
import com.jstarcraft.ai.math.structure.vector.ArrayVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class EuclideanHashFunction implements VectorHashFunction {

    private MathVector randomProjection;

    private int offset;

    private int w;

    public EuclideanHashFunction(Random random, int dimensions, int w) {
        this.w = w;
        this.offset = random.nextInt(w);

        randomProjection = new ArrayVector(dimensions, new float[dimensions]);
        for (int dimension = 0; dimension < dimensions; dimension++) {
            // mean 0
            // standard deviation 1.0
            float value = (float) random.nextGaussian();
            randomProjection.setValue(dimension, value);
        }
    }

    public int hash(MathVector vector) {
        MathScalar scalar = DefaultScalar.getInstance();
        float hash = (scalar.dotProduct(vector, randomProjection).getValue() + offset) / Float.valueOf(w);
        return Math.round(hash);
    }
}
