package be.tarsos.lsh.families;

import java.util.Random;

import be.tarsos.lsh.KeyVector;

public class CosineHash implements HashFunction {
    /**
     * 
     */
    private static final long serialVersionUID = 778951747630668248L;
    final KeyVector randomProjection;

    public CosineHash(Random rand, int dimensions) {
        randomProjection = new KeyVector("random", dimensions);
        for (int d = 0; d < dimensions; d++) {
            // mean 0
            // standard deviation 1.0
            float val = (float) rand.nextGaussian();
            randomProjection.setValue(d, val);
        }
    }

    @Override
    public int hash(KeyVector vector) {
        // calculate the dot product.
        float result = vector.dot(randomProjection);
        // returns a 'bit' encoded as an integer.
        // 1 when positive or zero, 0 otherwise.
        return result > 0 ? 1 : 0;
    }
}
