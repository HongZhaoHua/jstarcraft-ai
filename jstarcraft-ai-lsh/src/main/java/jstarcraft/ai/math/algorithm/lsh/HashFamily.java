package jstarcraft.ai.math.algorithm.lsh;

import java.util.Random;

import com.jstarcraft.core.common.hash.HashFunction;

public interface HashFamily<T extends HashFunction> {

    T getHashFunction(Random random);

}
