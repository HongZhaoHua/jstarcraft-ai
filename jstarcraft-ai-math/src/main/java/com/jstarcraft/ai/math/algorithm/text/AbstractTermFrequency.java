package com.jstarcraft.ai.math.algorithm.text;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.IntSet;

public abstract class AbstractTermFrequency implements TermFrequency {

	protected Int2FloatMap keyValues;

	protected int length;

	protected AbstractTermFrequency(Int2FloatMap keyValues, int length) {
		this.keyValues = keyValues;
		this.length = length;
	}

	@Override
	public IntSet getKeys() {
		return keyValues.keySet();
	}

	@Override
	public float getValue(int key) {
		return keyValues.get(key);
	}

	@Override
	public int getLength() {
		return length;
	}

}
