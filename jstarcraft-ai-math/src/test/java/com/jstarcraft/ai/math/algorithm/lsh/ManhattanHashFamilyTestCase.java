package com.jstarcraft.ai.math.algorithm.lsh;

import com.jstarcraft.core.utility.RandomUtility;

public class ManhattanHashFamilyTestCase extends LshHashFamilyTestCase {

    @Override
    protected LshHashFamily getHashFamily(int dimensions) {
        return new ManhattanHashFamily(dimensions, dimensions);
    }

    @Override
    protected float getRandomData() {
        return RandomUtility.randomFloat(1F);
    }

}
