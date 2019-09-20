package be.tarsos.lsh.families;

import java.util.Arrays;
import java.util.Random;

import com.jstarcraft.ai.math.structure.vector.MathVector;

import be.tarsos.lsh.KeyVector;

public class CityBlockHash implements HashFunction {
    /**
     * 
     */
    private static final long serialVersionUID = -635398900309516287L;
    private int w;
    private MathVector randomPartition;

    public CityBlockHash(Random rand, int dimensions, int width) {
        this.w = width;

        randomPartition = new KeyVector("random", dimensions);
        for (int d = 0; d < dimensions; d++) {
            // mean 0
            // standard deviation 1.0
            float val = rand.nextFloat() * w;
            randomPartition.setValue(d, val);
        }
    }

    public int hash(MathVector vector) {
        int hash[] = new int[randomPartition.getDimensionSize()];
        for (int d = 0; d < randomPartition.getDimensionSize(); d++) {
            hash[d] = (int) Math.floor((vector.getValue(d) - randomPartition.getValue(d)) / Float.valueOf(w));
        }
        return Arrays.hashCode(hash);
    }

    public String toString() {
        return String.format("w:%d\nrandomPartition:%s", w, randomPartition);
    }
}
