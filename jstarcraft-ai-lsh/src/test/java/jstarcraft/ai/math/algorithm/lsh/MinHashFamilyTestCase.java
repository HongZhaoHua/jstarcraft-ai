package jstarcraft.ai.math.algorithm.lsh;

public class MinHashFamilyTestCase extends LshHashFamilyTestCase {

    @Override
    protected LshHashFamily getHashFamily(int dimensions) {
        return new MinHashFamily();
    }

}
