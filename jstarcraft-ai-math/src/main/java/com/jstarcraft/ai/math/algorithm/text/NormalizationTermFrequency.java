package com.jstarcraft.ai.math.algorithm.text;

import java.util.Collection;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;

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
public class NormalizationTermFrequency extends AbstractTermFrequency {

	public NormalizationTermFrequency(Int2FloatMap keyValues, float k, int... document) {
		super(keyValues, document.length);
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
	}

	public NormalizationTermFrequency(Int2FloatMap keyValues, float k, Collection<Integer> document) {
		super(keyValues, document.size());
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
	}

}
