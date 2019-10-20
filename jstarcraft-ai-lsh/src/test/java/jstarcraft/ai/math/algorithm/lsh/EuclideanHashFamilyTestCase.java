package jstarcraft.ai.math.algorithm.lsh;

import com.jstarcraft.core.utility.RandomUtility;

public class EuclideanHashFamilyTestCase extends LshHashFamilyTestCase {

    @Override
    protected LshHashFamily getHashFamily(int dimensions) {
        return new EuclideanHashFamily(dimensions, dimensions);
    }

    @Override
    protected float getRandomData() {
        return RandomUtility.randomFloat(1F);
    }

}
