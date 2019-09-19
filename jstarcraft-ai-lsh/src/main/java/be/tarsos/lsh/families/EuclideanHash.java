package be.tarsos.lsh.families;

import java.util.Random;

import be.tarsos.lsh.KeyVector;

public class EuclideanHash implements HashFunction {
    /**
     * 
     */
    private static final long serialVersionUID = -3784656820380622717L;
    private KeyVector randomProjection;
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

    public int hash(KeyVector vector) {
        double hashValue = (vector.dot(randomProjection) + offset) / Float.valueOf(w);
        return (int) Math.round(hashValue);
    }
}
