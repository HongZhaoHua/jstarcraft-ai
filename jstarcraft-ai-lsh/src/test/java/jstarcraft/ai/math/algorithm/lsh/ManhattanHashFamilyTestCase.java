package jstarcraft.ai.math.algorithm.lsh;

public class ManhattanHashFamilyTestCase extends LshHashFamilyTestCase {

    @Override
    protected LshHashFamily getHashFamily(int dimensions) {
        return new ManhattanHashFamily(dimensions, dimensions);
    }

}
