package com.jstarcraft.ai.math.algorithm.text;

import java.util.Collection;

import org.apache.commons.math3.util.FastMath;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;

/**
 * Term Frequency
 * 
 * <pre>
 * log
 * </pre>
 * 
 * @author Birdy
 *
 */
public class LogarithmTermFrequency extends AbstractTermFrequency {

	public LogarithmTermFrequency(Int2FloatMap keyValues, int... document) {
		super(keyValues, document.length);
		for (int term : document) {
			keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
		}
		for (int term : keyValues.keySet()) {
			keyValues.put(term, (float) FastMath.log(1F + keyValues.get(term)));
		}
	}

	public LogarithmTermFrequency(Int2FloatMap keyValues, Collection<Integer> document) {
		super(keyValues, document.size());
		for (int term : document) {
			keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
		}
		for (int term : keyValues.keySet()) {
			keyValues.put(term, (float) FastMath.log(1F + keyValues.get(term)));
		}
	}

}
