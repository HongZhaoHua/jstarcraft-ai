package be.tarsos.lsh.families;

import java.util.Arrays;
import java.util.Random;

import be.tarsos.lsh.Vector;

public class CityBlockHash implements HashFunction {
    /**
     * 
     */
    private static final long serialVersionUID = -635398900309516287L;
    private int w;
    private Vector randomPartition;

    public CityBlockHash(Random rand, int dimensions, int width) {
        this.w = width;

        randomPartition = new Vector(dimensions);
        for (int d = 0; d < dimensions; d++) {
            // mean 0
            // standard deviation 1.0
            float val = rand.nextFloat() * w;
            randomPartition.set(d, val);
        }
    }

    public int hash(Vector vector) {
        int hash[] = new int[randomPartition.getDimensions()];
        for (int d = 0; d < randomPartition.getDimensions(); d++) {
            hash[d] = (int) Math.floor((vector.get(d) - randomPartition.get(d)) / Float.valueOf(w));
        }
        return Arrays.hashCode(hash);
    }

    public String toString() {
        return String.format("w:%d\nrandomPartition:%s", w, randomPartition);
    }
}
