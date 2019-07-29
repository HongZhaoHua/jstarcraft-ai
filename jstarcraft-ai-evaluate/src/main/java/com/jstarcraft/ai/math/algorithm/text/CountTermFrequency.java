package com.jstarcraft.ai.math.algorithm.text;

import java.util.Collection;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Term Frequency
 * 
 * <pre>
 * raw count
 * </pre>
 * 
 * @author Birdy
 *
 */
public class CountTermFrequency implements TermFrequency {

	private Int2FloatMap keyValues;

	public CountTermFrequency(Int2FloatMap keyValues, int... document) {
		for (int term : document) {
			keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
		}
		this.keyValues = keyValues;
	}

	public CountTermFrequency(Int2FloatMap keyValues, Collection<Integer> document) {
		for (int term : document) {
			keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
		}
		this.keyValues = keyValues;
	}

	@Override
	public IntSet getKeys() {
		return keyValues.keySet();
	}

	@Override
	public float getValue(int key) {
		return keyValues.get(key);
	}

}
