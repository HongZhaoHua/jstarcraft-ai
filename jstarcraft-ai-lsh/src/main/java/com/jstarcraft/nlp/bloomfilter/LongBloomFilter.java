package com.jstarcraft.nlp.bloomfilter;

import java.util.Random;

import com.jstarcraft.nlp.bloomfilter.bit.LongMap;

public class LongBloomFilter extends LocalBloomFilter {

    public LongBloomFilter(int bitSize, StringHashFamily hashFamily, int hashSize, Random random) {
        super(new LongMap(bitSize), getFunctions(hashFamily, hashSize, random));
    }

}
