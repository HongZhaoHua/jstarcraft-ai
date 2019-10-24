package com.jstarcraft.ai.math.algorithm.lsh;

import java.util.Random;

import com.jstarcraft.ai.math.algorithm.correlation.distance.EuclideanDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class EuclideanHashFamily implements LshHashFamily {

    private static final EuclideanDistance distance = new EuclideanDistance();

    private final int dimensions;

    private int w;

    public EuclideanHashFamily(int w, int dimensions) {
        this.dimensions = dimensions;
        this.w = w;
    }

    @Override
    public VectorHashFunction getHashFunction(Random random) {
        return new EuclideanHashFunction(random, dimensions, w);
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        return distance.getCoefficient(leftVector, rightVector);
    }

}
