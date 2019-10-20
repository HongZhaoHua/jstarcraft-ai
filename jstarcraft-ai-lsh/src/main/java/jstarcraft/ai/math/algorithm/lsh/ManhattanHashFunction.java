package jstarcraft.ai.math.algorithm.lsh;

import java.util.Arrays;
import java.util.Random;

import com.jstarcraft.ai.math.structure.vector.ArrayVector;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class ManhattanHashFunction implements VectorHashFunction {

    private int w;

    private MathVector randomPartition;

    public ManhattanHashFunction(Random random, int dimensions, int width) {
        this.w = width;

        randomPartition = new ArrayVector(dimensions, new float[dimensions]);
        for (int dimension = 0; dimension < dimensions; dimension++) {
            // mean 0
            // standard deviation 1.0
            float val = random.nextFloat() * w;
            randomPartition.setValue(dimension, val);
        }
    }

    public int hash(MathVector vector) {
        int hash[] = new int[randomPartition.getDimensionSize()];
        for (int dimension = 0; dimension < randomPartition.getDimensionSize(); dimension++) {
            hash[dimension] = (int) Math.floor((vector.getValue(dimension) - randomPartition.getValue(dimension)) / Float.valueOf(w));
        }
        return Arrays.hashCode(hash);
    }

    public String toString() {
        return String.format("w:%d\nrandomPartition:%s", w, randomPartition);
    }
}
