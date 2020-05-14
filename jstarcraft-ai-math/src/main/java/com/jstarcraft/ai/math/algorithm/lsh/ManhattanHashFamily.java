package com.jstarcraft.ai.math.algorithm.lsh;

import java.util.Random;

import com.jstarcraft.ai.math.algorithm.correlation.distance.ManhattanDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class ManhattanHashFamily implements LshHashFamily {

    private static final ManhattanDistance distance = new ManhattanDistance();

    private int dimensions;

    private int w;

    public ManhattanHashFamily(int dimensions, int w) {
        this.dimensions = dimensions;
        this.w = w;
    }

    @Override
    public VectorHashFunction getHashFunction(Random random) {
        return new ManhattanHashFunction(random, dimensions, w);
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        return distance.getCoefficient(leftVector, rightVector);
    }

}
