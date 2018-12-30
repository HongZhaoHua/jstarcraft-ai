package com.jstarcraft.ai.text;

import java.util.Collection;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Term Frequency
 * 
 * <pre>
 * term frequency
 * </pre>
 * 
 * @author Birdy
 *
 */
public class NaturalTermFrequency implements TermFrequency {

	private Int2FloatMap keyValues;

	public NaturalTermFrequency(Int2FloatMap keyValues, int... document) {
		for (int term : document) {
			keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
		}
		int size = document.length;
		for (int term : keyValues.keySet()) {
			keyValues.put(term, keyValues.get(term) / size);
		}
		this.keyValues = keyValues;
	}

	public NaturalTermFrequency(Int2FloatMap keyValues, Collection<Integer> document) {
		for (int term : document) {
			keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
		}
		int size = document.size();
		for (int term : keyValues.keySet()) {
			keyValues.put(term, keyValues.get(term) / size);
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
