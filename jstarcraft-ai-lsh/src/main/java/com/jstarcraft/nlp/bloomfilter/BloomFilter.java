package com.jstarcraft.nlp.bloomfilter;

/**
 * 布隆过滤器
 * 
 * @author Birdy
 *
 */
public interface BloomFilter {

    boolean get(String data);

    void put(String data);

}