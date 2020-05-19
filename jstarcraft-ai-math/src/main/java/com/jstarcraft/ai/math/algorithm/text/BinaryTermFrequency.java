package com.jstarcraft.ai.math.algorithm.text;

import java.util.Collection;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;

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
public class BinaryTermFrequency extends AbstractTermFrequency {

	public BinaryTermFrequency(Int2FloatMap keyValues, int... document) {
		super(keyValues, document.length);
		for (int term : document) {
			keyValues.put(term, 1F);
		}
	}

	public BinaryTermFrequency(Int2FloatMap keyValues, Collection<Integer> document) {
		super(keyValues, document.size());
		for (int term : document) {
			keyValues.put(term, 1F);
		}
	}

}
