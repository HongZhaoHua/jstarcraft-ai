package com.jstarcraft.ai.math.algorithm.text;

import java.util.Collection;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Term Frequency
 * 
 * <pre>
 * normalization K
 * </pre>
 * 
 * @author Birdy
 *
 */
public class NormalizationTermFrequency implements TermFrequency {

	private Int2FloatMap keyValues;

	public NormalizationTermFrequency(Int2FloatMap keyValues, float k, int... document) {
		assert k >= 0 && k < 1;
		float denominator = 0F;
		for (int term : document) {
			float numerator = keyValues.getOrDefault(term, 0F) + 1F;
			keyValues.put(term, numerator);
			if (numerator > denominator) {
				denominator = numerator;
			}
		}
		for (int term : keyValues.keySet()) {
			keyValues.put(term, k + (1 - k) * keyValues.get(term) / denominator);
		}
		this.keyValues = keyValues;
	}

	public NormalizationTermFrequency(Int2FloatMap keyValues, float k, Collection<Integer> document) {
		assert k >= 0 && k < 1;
		float denominator = 0F;
		for (int term : document) {
			float numerator = keyValues.getOrDefault(term, 0F) + 1F;
			keyValues.put(term, numerator);
			if (numerator > denominator) {
				denominator = numerator;
			}
		}
		for (int term : keyValues.keySet()) {
			keyValues.put(term, k + (1 - k) * keyValues.get(term) / denominator);
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
