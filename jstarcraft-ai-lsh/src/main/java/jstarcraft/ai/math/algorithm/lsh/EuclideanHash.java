package jstarcraft.ai.math.algorithm.lsh;

import java.util.Random;

import com.jstarcraft.ai.math.structure.DefaultScalar;
import com.jstarcraft.ai.math.structure.MathScalar;
import com.jstarcraft.ai.math.structure.vector.MathVector;

import be.tarsos.lsh.KeyVector;

public class EuclideanHash implements HashFunction {
    /**
     * 
     */
    private static final long serialVersionUID = -3784656820380622717L;
    private MathVector randomProjection;
    private int offset;
    private int w;

    public EuclideanHash(Random rand, int dimensions, int w) {
        this.w = w;
        this.offset = rand.nextInt(w);

        randomProjection = new KeyVector("random", dimensions);
        for (int d = 0; d < dimensions; d++) {
            // mean 0
            // standard deviation 1.0
            float val = (float) rand.nextGaussian();
            randomProjection.setValue(d, val);
        }
    }

    public int hash(MathVector vector) {
        MathScalar scalar = DefaultScalar.getInstance();
        float hashValue = (scalar.dotProduct(vector, randomProjection).getValue() + offset) / Float.valueOf(w);
        return Math.round(hashValue);
    }
}
