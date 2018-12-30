package com.jstarcraft.ai.text;

import java.util.Collection;

import org.apache.commons.math3.util.FastMath;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.IntSet;

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
public class LogarithmTermFrequency implements TermFrequency {

	private Int2FloatMap keyValues;

	public LogarithmTermFrequency(Int2FloatMap keyValues, int... document) {
		for (int term : document) {
			keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
		}
		for (int term : keyValues.keySet()) {
			keyValues.put(term, (float) FastMath.log(1F + keyValues.get(term)));
		}
		this.keyValues = keyValues;
	}

	public LogarithmTermFrequency(Int2FloatMap keyValues, Collection<Integer> document) {
		for (int term : document) {
			keyValues.put(term, keyValues.getOrDefault(term, 0F) + 1F);
		}
		for (int term : keyValues.keySet()) {
			keyValues.put(term, (float) FastMath.log(1F + keyValues.get(term)));
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
