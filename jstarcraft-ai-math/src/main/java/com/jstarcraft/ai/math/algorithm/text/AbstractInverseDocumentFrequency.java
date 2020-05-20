package com.jstarcraft.ai.math.algorithm.text;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.IntSet;

public abstract class AbstractInverseDocumentFrequency implements InverseDocumentFrequency {

	protected Int2FloatMap keyValues;

	protected AbstractInverseDocumentFrequency(Int2FloatMap keyValues) {
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
