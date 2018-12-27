package com.jstarcraft.ai.text;

import java.util.Collection;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Term Frequency
 * 
 * <pre>
 * 1 if term is existent, 0 if term is non-existent
 * </pre>
 * 
 * @author Birdy
 *
 */
public class BinaryTermFrequency implements TermFrequency {

	private Int2FloatMap keyValues;

	public BinaryTermFrequency(Int2FloatMap keyValues, int... document) {
		for (int term : document) {
			keyValues.put(term, 1F);
		}
		this.keyValues = keyValues;
	}

	public BinaryTermFrequency(Int2FloatMap keyValues, Collection<Integer> document) {
		for (int term : document) {
			keyValues.put(term, 1F);
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
