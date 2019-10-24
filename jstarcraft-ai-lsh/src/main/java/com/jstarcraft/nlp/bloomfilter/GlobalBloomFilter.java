package com.jstarcraft.nlp.bloomfilter;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;

public class GlobalBloomFilter implements BloomFilter {

    private RBloomFilter<String> bloomFilter;

    public GlobalBloomFilter(Redisson redisson, String name) {
        this.bloomFilter = redisson.getBloomFilter(name);
    }

    public GlobalBloomFilter(Redisson redisson, String name, int elments, float probability) {
        this.bloomFilter = redisson.getBloomFilter(name);
        if (!this.bloomFilter.tryInit(elments, probability)) {
            throw new RuntimeException("布隆过滤器冲突");
        }
    }

    @Override
    public boolean get(String data) {
        return bloomFilter.contains(data);
    }

    @Override
    public void put(String data) {
        bloomFilter.add(data);
    }

}
