package jstarcraft.ai.math.algorithm.lsh;

public class EuclideanHashFamilyTestCase extends LshHashFamilyTestCase {

    @Override
    protected LshHashFamily getHashFamily(int dimensions) {
        return new EuclideanHashFamily(dimensions, dimensions);
    }

}
