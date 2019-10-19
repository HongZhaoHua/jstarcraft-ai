package jstarcraft.ai.math.algorithm.lsh;

public class CosineHashFamilyTestCase extends LshHashFamilyTestCase {

    @Override
    protected LshHashFamily getHashFamily(int dimensions) {
        return new CosineHashFamily(dimensions);
    }

}
