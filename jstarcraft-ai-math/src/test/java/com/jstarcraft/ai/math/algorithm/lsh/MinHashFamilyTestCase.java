package com.jstarcraft.ai.math.algorithm.lsh;

import java.util.Random;

import com.jstarcraft.core.utility.RandomUtility;

public class MinHashFamilyTestCase extends LshHashFamilyTestCase {

    @Override
    protected LshHashFamily getHashFamily(Random random, int dimensions) {
        return new MinHashFamily();
    }

    @Override
    protected float getRandomData() {
        return RandomUtility.randomInteger(2);
    }

}
