package com.jstarcraft.nlp.bloomfilter;

import java.util.Random;

import com.jstarcraft.nlp.bloomfilter.bit.IntegerMap;

public class IntegerBloomFilter extends LocalBloomFilter {

    public IntegerBloomFilter(int bitSize, StringHashFamily hashFamily, int hashSize, Random random) {
        super(new IntegerMap(bitSize), getFunctions(hashFamily, hashSize, random));
    }

}
