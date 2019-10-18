package jstarcraft.ai.math.structure.bloomfilter;

import java.util.Random;

import jstarcraft.ai.math.structure.bloomfilter.bit.IntegerMap;

public class IntegerBloomFilter extends LocalBloomFilter {

    protected IntegerBloomFilter(int bitSize, StringHashFamily hashFamily, int hashSize, Random random) {
        super(new IntegerMap(bitSize), getFunctions(hashFamily, hashSize, random));
    }

}
