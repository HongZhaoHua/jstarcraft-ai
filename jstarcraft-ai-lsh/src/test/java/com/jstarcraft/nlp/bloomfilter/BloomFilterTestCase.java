package com.jstarcraft.nlp.bloomfilter;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.nlp.bloomfilter.BloomFilter;

public abstract class BloomFilterTestCase {

    protected abstract BloomFilter getBloomFilter(int elments, float probability);

    @Test
    public void testBloomFilter() {
        int elments = 1000;
        float probability = 0.001F;
        BloomFilter bloomFilter = getBloomFilter(elments, probability);
        int times = 0;
        for (int index = 0; index < elments; index++) {
            String data = String.valueOf(index);
            if (bloomFilter.get(data)) {
                times++;
            }
            bloomFilter.put(data);
            Assert.assertTrue(bloomFilter.get(data));
        }
        Assert.assertTrue(times < elments * probability);
    }
    
}
