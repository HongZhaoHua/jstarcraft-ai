package com.jstarcraft.ai.math.algorithm.text;

import java.util.Collection;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;

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
public class CountTermFrequency extends AbstractTermFrequency {

	public CountTermFrequency(Int2FloatMap keyValues, int... document) {
		super(keyValues, document.length);
		for (int term : document) {
			keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
		}
	}

	public CountTermFrequency(Int2FloatMap keyValues, Collection<Integer> document) {
		super(keyValues, document.size());
		for (int term : document) {
			keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
		}
	}

}
