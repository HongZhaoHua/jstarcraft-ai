package com.jstarcraft.ai.math.algorithm.text;

import java.util.Collection;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;

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
public class NaturalTermFrequency extends AbstractTermFrequency {

	public NaturalTermFrequency(Int2FloatMap keyValues, int... document) {
		super(keyValues, document.length);
		for (int term : document) {
			keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
		}
		for (int term : keyValues.keySet()) {
			keyValues.put(term, keyValues.get(term) / length);
		}
	}

	public NaturalTermFrequency(Int2FloatMap keyValues, Collection<Integer> document) {
		super(keyValues, document.size());
		for (int term : document) {
			keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
		}
		for (int term : keyValues.keySet()) {
			keyValues.put(term, keyValues.get(term) / length);
		}
	}

}
